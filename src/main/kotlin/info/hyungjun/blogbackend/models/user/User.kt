package info.hyungjun.blogbackend.models.user

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("User")
data class User(
  @Id
  val id: Long?,
  val email: String,
  val password: String,
  var salt: String,
  var role: UserRoles,
  @Column(value = "createdAt")
  val createdAt: LocalDateTime
)
