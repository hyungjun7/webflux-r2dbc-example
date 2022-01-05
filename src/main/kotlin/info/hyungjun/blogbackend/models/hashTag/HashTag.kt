package info.hyungjun.blogbackend.models.hashTag

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(value = "HashTag")
data class HashTag(
  @Id
  var id: Long?,
  val name: String
)