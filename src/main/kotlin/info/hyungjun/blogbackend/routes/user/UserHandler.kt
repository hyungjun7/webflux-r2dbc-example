package info.hyungjun.blogbackend.routes.user

import info.hyungjun.blogbackend.common.DuplicateException
import info.hyungjun.blogbackend.models.user.UserCreate
import info.hyungjun.blogbackend.models.user.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class UserHandler(
  private val userService: UserService
) {
  suspend fun postUser(req: ServerRequest): ServerResponse {
    return try {
      val id = userService.createUser(req.awaitBody())
      ServerResponse.status(HttpStatus.CREATED).bodyValueAndAwait(id)
    } catch (e: DuplicateException) {
      ServerResponse.status(HttpStatus.CONFLICT).buildAndAwait()
    }
  }
  
  suspend fun getUser(req: ServerRequest): ServerResponse {
    return ServerResponse.ok().bodyValueAndAwait(userService.findUser())
  }
}