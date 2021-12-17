package info.hyungjun.blogbackend.models.user

import info.hyungjun.blogbackend.exceptions.DuplicateException
import info.hyungjun.blogbackend.exceptions.NotFoundException
import info.hyungjun.blogbackend.modules.JwtModule
import org.mindrot.jbcrypt.BCrypt
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service

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
      val insertData = userRepository.post(CreateUserDao(data.email, password, salt, UserRoles.Admin))
      val user = userRepository.findOne(FindUserDao(null, insertData["insert_id"] as Long))?: throw NotFoundException()
      PostUserRespDTO(
        user = GetUserRespDTO(user.id, user.email, user.created_at.toString()),
        accessToken = jwtModule.createAccessToken(user.id)
      )
    } catch (e: DataIntegrityViolationException) {
      throw DuplicateException()
    }
  }
  
  @Throws(NotFoundException::class)
  suspend fun findUser(id: Long): GetUserRespDTO {
    val user = userRepository.findOne(FindUserDao(null, id)) ?: throw NotFoundException()
    return GetUserRespDTO(user.id, user.email, user.created_at.toString())
  }
}