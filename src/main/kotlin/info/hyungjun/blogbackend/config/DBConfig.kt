package info.hyungjun.blogbackend.config

import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration
import dev.miku.r2dbc.mysql.MySqlConnectionFactory
import io.github.cdimascio.dotenv.dotenv
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
internal class DBConfig: AbstractR2dbcConfiguration() {
  @Bean
  override fun connectionFactory(): ConnectionFactory {
    val env = dotenv()
    return MySqlConnectionFactory.from(
      MySqlConnectionConfiguration.builder()
        .host(env["database_host"])
        .port(Integer.parseInt(env["database_port"]))
        .database(env["database_name"])
        .username(env["database_username"])
        .password(env["database_password"])
        .build()
    )
  }
  
  @Bean
  fun transactionManager(connectionFactory: ConnectionFactory): ReactiveTransactionManager {
    return R2dbcTransactionManager(connectionFactory)
  }
}