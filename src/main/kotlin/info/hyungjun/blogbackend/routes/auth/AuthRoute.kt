package info.hyungjun.blogbackend.routes.auth
import info.hyungjun.blogbackend.common.GlobalExceptionDTO
import info.hyungjun.blogbackend.models.auth.PostAuthReqDTO
import info.hyungjun.blogbackend.models.user.PostUserRespDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class AuthRoutes {
  companion object AuthRoute {
    const val path = "/auth"
  }
  @Bean
  @RouterOperations(
    RouterOperation(
      path = path,
      method = [RequestMethod.POST],
      beanClass = AuthHandler::class,
      beanMethod = "postAuth",
      operation = Operation(
        tags = ["Auth"],
        summary = "유저 로그인 API",
        operationId = "PostAuth",
        requestBody = RequestBody(
          content = [Content(
            schema = Schema(
              implementation = PostAuthReqDTO::class
            )
          )]
        ),
        responses = [
          ApiResponse(
            responseCode = "200",
            content = [Content(schema = Schema(implementation = PostUserRespDTO::class))]
          ),
          ApiResponse(
            responseCode = "404",
            description = "cannot find email",
            content = [Content(schema = Schema(implementation = GlobalExceptionDTO::class))]
          ),
          ApiResponse(
            responseCode = "409",
            description = "wrong password",
            content = [Content(schema = Schema(implementation = GlobalExceptionDTO::class))]
          )
        ]
      )
    )
  )
  fun authRoute(handler: AuthHandler) = coRouter {
    path.nest {
      accept(MediaType.APPLICATION_JSON).nest {
        POST("", handler::postAuth)
      }
    }
  }
}
