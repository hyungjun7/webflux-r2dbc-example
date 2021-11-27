package info.hyungjun.blogbackend.models.user

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("User")
data class User(
  @Id
  val id: Long,
  val email: String,
  val password: String,
  var salt: String,
  var role: UserRoles,
  val created_at: LocalDateTime
)

data class PostUserReqDTO(
  val email: String,
  val password: String,
)

data class PostUserRespDTO(
  val id: Long,
  val email: String,
  val created_at: LocalDateTime
)

enum class UserRoles {
  Admin,
  User
}
