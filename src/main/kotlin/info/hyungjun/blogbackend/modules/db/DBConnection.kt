package info.hyungjun.blogbackend.modules.db

import io.github.cdimascio.dotenv.dotenv
import io.r2dbc.pool.ConnectionPool
import io.r2dbc.pool.ConnectionPoolConfiguration
import io.r2dbc.spi.*
import io.r2dbc.spi.ConnectionFactoryOptions.*
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactor.awaitSingle
import reactor.core.publisher.Flux
import java.time.Duration
import kotlin.reflect.full.declaredMemberProperties

class DBConnection private constructor() {
  companion object {
    @Volatile private var instance: DBConnection? = null
    
    @JvmStatic fun getInstance(): DBConnection =
      instance ?: synchronized(this) {
        instance ?: DBConnection().also {
          instance = it
        }
      }
  }
  
  private val env = dotenv()
  
  private val connectionFactory: ConnectionFactory = ConnectionFactories.get(
    builder()
      .option(DRIVER, "pool")
      .option(PROTOCOL, "mysql")
      .option(HOST, env["database_host"])
      .option(PORT, Integer.parseInt(env["database_port"]))
      .option(USER, env["database_username"])
      .option(PASSWORD, env["database_password"])
      .option(DATABASE, env["database_name"])
      .build()
  )
  
  private val pool = ConnectionPool(
    ConnectionPoolConfiguration.builder(connectionFactory)
      .maxIdleTime(Duration.ofMillis(3000))
      .maxSize(15)
      .build()
  )
  
  suspend fun getConnection(connection: Connection?): Connection {
    return connection ?: pool.create().awaitSingle()
  }
  
  private fun setStatement(q: String, values: List<IBindSQLValue>, conn: Connection): Statement {
    val statement = conn.createStatement(q)
    if (values.isNotEmpty()) {
      for (item in values) {
        statement.bind(item.index, item.value)
      }
    }
    return statement
  }
  
  suspend fun beginTransaction(): Connection {
    val connection = pool.create().awaitSingle()
    connection.beginTransaction()
    return connection
  }
  
  suspend fun commitTransaction(connection: Connection): Void? {
    try {
      return connection.commitTransaction().awaitSingle()
    } finally {
      connection.close()
    }
  }
  
  suspend fun rollbackTransaction(connection: Connection): Void? {
    try {
      return connection.rollbackTransaction().awaitSingle()
    } finally {
      connection.close()
    }
  }
  
  suspend fun selectMany(q: String, values: List<IBindSQLValue>, connection: Connection?): List<MutableMap<String, Any>> {
    val conn = getConnection(connection)
    return Flux.from(setStatement(q, values, conn).execute())
      .doFinally { conn.close() }
      .flatMap {
        it.map { row, meta ->
          val result: MutableMap<String, Any> = mutableMapOf()
          for (item in meta.columnNames) {
            result[item] = row.get(item) as Any
          }
          result
        }
      }
      .asFlow().toList()
  }
  
  suspend fun selectOne(q: String, values: List<IBindSQLValue>, connection: Connection?): MutableMap<String, Any> {
    val conn = getConnection(connection)
    try {
      return setStatement(q, values, conn).execute()
        .awaitFirst()
        .map { row, meta ->
          val result: MutableMap<String, Any> = mutableMapOf()
          for (item in meta.columnNames) {
            result[item] = row.get(item) as Any
          }
          result
        }
        .awaitFirst()
    } finally {
      conn.close()
    }
  }
  
  suspend fun insert(q: String, value: Any, connection: Connection?): MutableMap<String, Any> {
    val conn = getConnection(connection)
    try {
      val columnNames = mutableListOf<String>()
      val valueNames = mutableListOf<Any?>()
      val bindCount = mutableListOf<String>()
      value::class.declaredMemberProperties.forEach {
        columnNames.add(it.name)
        valueNames.add(it.getter.call(value))
        bindCount.add("?")
      }
      val query = q.replace("??", columnNames.joinToString()).replace("?", bindCount.joinToString())
      println(query)
      val statement = conn.createStatement(query)
      for ((idx, field) in valueNames.withIndex()) {
        statement.bind(idx, field as Any)
      }
      return statement.returnGeneratedValues("insert_id").execute()
        .awaitFirst()
        .map { row, meta ->
          val result: MutableMap<String, Any> = mutableMapOf()
          for (item in meta.columnNames) {
            result[item] = row.get(item) as Any
          }
          result
        }
        .awaitFirst()
    } finally {
      conn.close()
    }
  }
}