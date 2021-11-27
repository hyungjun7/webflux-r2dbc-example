package info.hyungjun.blogbackend.routes.user

import info.hyungjun.blogbackend.models.user.PostUserReqDTO
import info.hyungjun.blogbackend.models.user.UserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class UserHandler(
  private val userService: UserService
) {
  suspend fun postUser(req: ServerRequest): ServerResponse {
    val data = req.awaitBody<PostUserReqDTO>()
    val user = userService.createUser(data)
    return ServerResponse.status(HttpStatus.CREATED).bodyValueAndAwait(user)
  }
  
  suspend fun getUser(req: ServerRequest): ServerResponse {
    return ServerResponse.ok().bodyValueAndAwait(userService.findUser())
  }
}