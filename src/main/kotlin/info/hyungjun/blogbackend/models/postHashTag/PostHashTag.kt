package info.hyungjun.blogbackend.models.postHashTag

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table(value = "Post-HashTag")
data class PostHashTag(
  @Id
  var id: Long?,
  @Column(value = "hashTagId")
  val hashTagId: Long,
  @Column(value = "postId")
  val postId: Long
)