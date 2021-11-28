package info.hyungjun.blogbackend.models.user

import info.hyungjun.blogbackend.common.DuplicateException
import info.hyungjun.blogbackend.modules.JwtModule
import org.mindrot.jbcrypt.BCrypt
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService(
  val userRepository: UserRepository,
  val jwtModule: JwtModule
) {
  
  @Throws(DuplicateException::class)
  suspend fun createUser(data: PostUserReqDTO): PostUserRespDTO {
    return try {
      val salt = BCrypt.gensalt(10)
      val password = BCrypt.hashpw(data.password, salt)
      val user = userRepository.save(User(0, data.email, password, salt, UserRoles.Admin, LocalDateTime.now()))
      PostUserRespDTO(
        user = GetUserRespDTO(user.id, user.email, user.created_at.toString()),
        accessToken = jwtModule.createAccessToken(user.id)
      )
    } catch (e: DataIntegrityViolationException) {
      throw DuplicateException(HttpStatus.CONFLICT, "already_in_use")
    }
  }
  
  suspend fun findUser(): List<GetUserRespDTO> {
    return userRepository.findUser()
      .map {
        GetUserRespDTO(it.id, it.email, it.created_at.toString())
      }
  }
}