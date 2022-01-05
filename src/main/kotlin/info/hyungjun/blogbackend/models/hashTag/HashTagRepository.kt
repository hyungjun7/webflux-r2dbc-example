package info.hyungjun.blogbackend.models.hashTag

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.core.select
import org.springframework.data.relational.core.query.Criteria
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class HashTagRepository(
  private val entityTemplate: R2dbcEntityTemplate,
  private val databaseClient: DatabaseClient
) {
  val tableName = "HashTag"
  fun insert(data: HashTag): Mono<Int> {
    return databaseClient.sql(
      "INSERT INTO $tableName (name) " +
        "SELECT :name FROM DUAL " +
        "WHERE NOT EXISTS(SELECT * FROM $tableName WHERE name = :name)"
    )
      .bind("name", data.name)
      .fetch()
      .rowsUpdated()
  }
  
  fun findByName(name: String): Mono<HashTag> {
    return databaseClient.sql("SELECT * FROM $tableName WHERE name = :name")
      .bind("name", name)
      .map { row ->
        HashTag(id = row["id"] as Long, name = row["name"] as String)
      }
      .one()
  }
}