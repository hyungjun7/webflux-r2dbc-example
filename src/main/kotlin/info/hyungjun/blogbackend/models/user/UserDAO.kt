package info.hyungjun.blogbackend.models.user

import info.hyungjun.blogbackend.common.BindModelDao
import java.time.LocalDateTime

data class CreateUserDao(
  val email: String,
  val password: String,
  val salt: String,
  val role: UserRoles
)

data class FindUserDao(
  val email: String?,
  val id: Long?
)

class UserDao(map: MutableMap<String, Any>): BindModelDao(map) {
  val id = map["id"] as Long
  val email = map["email"] as String
  val password = map["password"] as String
  val salt = map["salt"] as String
  val role = map["role"] as String
  val created_at = map["created_at"] as LocalDateTime
}