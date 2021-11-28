package info.hyungjun.blogbackend.routes.user
import info.hyungjun.blogbackend.common.GlobalExceptionDTO
import info.hyungjun.blogbackend.models.user.PostUserReqDTO
import info.hyungjun.blogbackend.models.user.PostUserRespDTO
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class UserRoutes {
  companion object UserRoute {
    const val path = "/user"
  }
  @Bean
  @RouterOperations(
    RouterOperation(
      path = path,
      method = [RequestMethod.POST],
      beanClass = UserHandler::class,
      beanMethod = "postUser",
      operation = Operation(
        tags = ["User"],
        summary = "유저 회원가입 API",
        operationId = "PostUser",
        requestBody = RequestBody(
          content = [Content(
            schema = Schema(
              implementation = PostUserReqDTO::class
            )
          )]
        ),
        responses = [
          ApiResponse(
            responseCode = "201",
            content = [Content(schema = Schema(implementation = PostUserRespDTO::class))]
          ),
          ApiResponse(
            responseCode = "409",
            description = "사용중인 이메일",
            content = [Content(schema = Schema(implementation = GlobalExceptionDTO::class))]
          )
        ]
      )
    ),
    RouterOperation(
      path = path,
      method = [RequestMethod.GET],
      beanClass = UserHandler::class,
      beanMethod = "getUser",
      operation = Operation(
        tags = ["User"],
        operationId = "GetUser",
        security = [SecurityRequirement(
          name = "bearerAuth",
          scopes = ["http"]
        )],
        responses = [
          ApiResponse(
            responseCode = "200",
            content = [Content(schema = Schema(implementation = PostUserRespDTO::class))]
          )
        ]
      )
    ),
  )
  fun userRoute(handler: UserHandler) = coRouter {
    path.nest {
      accept(MediaType.APPLICATION_JSON).nest {
        POST("", handler::postUser)
        GET("", handler::getUser)
      }
    }
  }
}
