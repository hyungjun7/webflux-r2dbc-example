package info.hyungjun.blogbackend.config

import info.hyungjun.blogbackend.routes.BlogRoute
import info.hyungjun.blogbackend.routes.user.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction

@Configuration
class RouterConfig {
  @Bean
  fun routerFunction(userHandler: UserHandler): RouterFunction<*> {
    return BlogRoute().route(userHandler)
  }
}