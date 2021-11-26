package info.hyungjun.blogbackend.models.user

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

open class UserCreate(
  open val email: String,
  open val password: String,
)

enum class UserRoles {
  Admin,
  User
}

@Table("User")
data class User(
  @Id
  val id: Long,
  override var email: String,
  override var password: String,
  var salt: String,
  var role: UserRoles,
  val createdAt: String
): UserCreate(email, password)
