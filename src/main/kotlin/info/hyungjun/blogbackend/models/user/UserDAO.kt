package info.hyungjun.blogbackend.models.user

import java.time.LocalDateTime

data class FindUser(
  val id: Long,
  val email: String,
  val created_at: LocalDateTime
)