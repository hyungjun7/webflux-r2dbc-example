package info.hyungjun.blogbackend.config

import dev.miku.r2dbc.mysql.MySqlConnectionConfiguration
import dev.miku.r2dbc.mysql.MySqlConnectionFactory
import io.github.cdimascio.dotenv.dotenv
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@EnableR2dbcRepositories
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
}