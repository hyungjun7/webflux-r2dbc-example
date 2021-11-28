package info.hyungjun.blogbackend.models.user

data class PostUserReqDTO(
  val email: String,
  val password: String,
)

data class GetUserRespDTO(
  val id: Long,
  val email: String,
  val created_at: String
)

data class PostUserRespDTO(
  val user: GetUserRespDTO,
  val accessToken: String
)