package info.hyungjun.blogbackend.models.post

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table(value = "Post")
data class Post(
  @Id
  var id: Long?,
  @Column(value = "userId")
  val userId: Long,
  val title: String,
  val content: String,
  @Column(value = "createdAt")
  var createdAt: LocalDateTime
)