package info.hyungjun.blogbackend.models.user

data class PostUserReqDTO(
  val email: String,
  val password: String,
)

data class PostUserRespDTO(
  val id: Long,
  val email: String,
  val created_at: String
)