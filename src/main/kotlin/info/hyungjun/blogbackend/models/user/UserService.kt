package info.hyungjun.blogbackend.models.user

import info.hyungjun.blogbackend.exceptions.DuplicateException
import info.hyungjun.blogbackend.exceptions.GlobalException
import info.hyungjun.blogbackend.exceptions.NotFoundException
import info.hyungjun.blogbackend.modules.JwtModule
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.mindrot.jbcrypt.BCrypt
import org.springframework.dao.DataIntegrityViolationException
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
      val user = userRepository.insert(
        User(
          id = null,
          email = data.email,
          password = password,
          salt = salt,
          role = UserRoles.Admin,
          createdAt = LocalDateTime.now()
        )
      ).awaitSingle()
      PostUserRespDTO(
        user = GetUserRespDTO(user.id!!, user.email, user.createdAt.toString()),
        accessToken = jwtModule.createAccessToken(user.id)
      )
    } catch (e: DataIntegrityViolationException) {
      throw DuplicateException()
    }
  }
  
  @Throws(NotFoundException::class)
  suspend fun findUser(id: Long): GetUserRespDTO {
    val user = userRepository.findByEmail(id.toString()).awaitSingleOrNull() ?: throw NotFoundException()
    return GetUserRespDTO(user.id!!, user.email, user.createdAt.toString())
  }
}