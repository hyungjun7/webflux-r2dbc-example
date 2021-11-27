package info.hyungjun.blogbackend.models.user

import info.hyungjun.blogbackend.common.DuplicateException
import kotlinx.coroutines.flow.toList
import org.mindrot.jbcrypt.BCrypt
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService(
  val userRepository: UserRepository
) {
  
  @Throws(DuplicateException::class)
  suspend fun createUser(data: PostUserReqDTO): PostUserRespDTO {
    return try {
      val salt = BCrypt.gensalt(10)
      val password = BCrypt.hashpw(data.password, salt)
      val user = userRepository.save(User(0, data.email, password, salt, UserRoles.Admin, LocalDateTime.now()))
      PostUserRespDTO(user.id, user.email, user.created_at)
    } catch (e: DataIntegrityViolationException) {
      throw DuplicateException(HttpStatus.CONFLICT, "already_in_use")
    }
  }
  
  suspend fun findUser(): List<User> {
    return userRepository.findAll().toList()
  }
}