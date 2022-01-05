package info.hyungjun.blogbackend.models.post

data class PostCreateDao(
  val title: String,
  val content: String,
  val userId: Long
)