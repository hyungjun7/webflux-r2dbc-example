package info.hyungjun.blogbackend.routes.post

import info.hyungjun.blogbackend.models.post.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.Parameters
import io.swagger.v3.oas.annotations.enums.ParameterIn
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
class PostRouter {
  companion object Path {
    const val path = "/post"
  }
  
  @Bean
  @RouterOperations(
    RouterOperation(
      path = path,
      method = [RequestMethod.POST],
      beanClass = PostHandler::class,
      beanMethod = "postPost",
      operation = Operation(
        tags = ["Post"],
        summary = "게시글 작성 API",
        operationId = "PostPost",
        security = [SecurityRequirement(
          name = "bearerAuth",
          scopes = ["http"]
        )],
        requestBody = RequestBody(
          content = [Content(
            schema = Schema(
              implementation = PostPostReqDto::class
            )
          )]
        ),
        responses = [
          ApiResponse(
            responseCode = "201",
            content = [Content(schema = Schema(implementation = GetPostRespDto::class))]
          )
        ]
      )
    ),
    RouterOperation(
      path = path,
      method = [RequestMethod.GET],
      beanClass = PostHandler::class,
      beanMethod = "getPosts",
      operation = Operation(
        tags = ["Post"],
        summary = "게시글 목록 API",
        operationId = "GetPosts",
        parameters = [
          Parameter(name = "start", `in` = ParameterIn.QUERY, required = true, schema = Schema(implementation = Int::class)),
          Parameter(name = "perPage", `in` = ParameterIn.QUERY, required = true, schema = Schema(implementation = Long::class)),
          Parameter(name = "hashTagId", `in` = ParameterIn.QUERY, schema = Schema(implementation = Long::class)),
          Parameter(name = "search", `in` = ParameterIn.QUERY, schema = Schema(implementation = String::class))
                     ],
        responses = [
          ApiResponse(
            responseCode = "200",
            content = [Content(schema = Schema(implementation = GetPostListRespDto::class))]
          )
        ]
      )
    )
  )
  fun postRoutes(handler: PostHandler) = coRouter {
    path.nest {
      accept(MediaType.APPLICATION_JSON).nest {
        POST("", handler::postPost)
        GET("", handler::getPosts)
      }
    }
  }
}