package info.hyungjun.blogbackend.models.auth

import info.hyungjun.blogbackend.exceptions.NotFoundException
import info.hyungjun.blogbackend.exceptions.WrongPasswordException
import info.hyungjun.blogbackend.models.user.FindUserDao
import info.hyungjun.blogbackend.models.user.GetUserRespDTO
import info.hyungjun.blogbackend.models.user.PostUserRespDTO
import info.hyungjun.blogbackend.models.user.UserRepository
import info.hyungjun.blogbackend.modules.JwtModule
import org.mindrot.jbcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class AuthService(
  val userRepository: UserRepository,
  val jwtModule: JwtModule
) {
  @Throws(
    NotFoundException::class,
    WrongPasswordException::class
  )
  suspend fun postAuth(data: PostAuthReqDTO): PostUserRespDTO {
    val user = userRepository.findOne(FindUserDao(data.email, null)) ?: throw NotFoundException()
    if (BCrypt.checkpw(data.password, user.password)) {
      return PostUserRespDTO(
        user = GetUserRespDTO(user.id, user.email, user.created_at.toString()),
        accessToken = jwtModule.createAccessToken(user.id)
      )
    }
    throw WrongPasswordException()
  }
}