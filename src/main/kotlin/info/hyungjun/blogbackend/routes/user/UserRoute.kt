package info.hyungjun.blogbackend.routes.user
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

class UserRoute {
  fun userRoute(handler: UserHandler) = coRouter {
    "/user".nest {
      accept(MediaType.ALL).nest {
        POST("", handler::postUser)
        GET("", handler::getUser)
      }
    }
  }
}
