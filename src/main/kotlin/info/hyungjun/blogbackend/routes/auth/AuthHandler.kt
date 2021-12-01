package info.hyungjun.blogbackend.routes.auth

import info.hyungjun.blogbackend.models.auth.AuthService
import info.hyungjun.blogbackend.models.auth.PostAuthReqDTO
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class AuthHandler(
  val authService: AuthService
) {
  suspend fun postAuth(req: ServerRequest): ServerResponse {
    val data = req.awaitBody<PostAuthReqDTO>()
    val user = authService.postAuth(data)
    return ServerResponse.status(HttpStatus.OK).bodyValueAndAwait(user)
  }
}