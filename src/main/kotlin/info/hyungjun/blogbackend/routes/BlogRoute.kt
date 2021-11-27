package info.hyungjun.blogbackend.routes

import info.hyungjun.blogbackend.routes.user.UserHandler
import info.hyungjun.blogbackend.routes.user.UserRoute
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

class BlogRoute {
  fun route(handler: UserHandler) = coRouter {
    "/api/blog".nest {
      accept(MediaType.ALL).nest {
        add(UserRoute().userRoute(handler))
      }
    }
  }
}