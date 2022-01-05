package info.hyungjun.blogbackend.models.post

import info.hyungjun.blogbackend.common.PaginationDto
import info.hyungjun.blogbackend.models.hashTag.HashTag
import info.hyungjun.blogbackend.models.hashTag.HashTagCreateDao

data class PostPostReqDto(
  val title: String,
  val content: String,
  val hashTags: List<HashTagCreateDao>
)

data class PostPostWithUserIdDto(
  val title: String,
  val content: String,
  val userId: Long,
  val hashTags: List<HashTagCreateDao>
)

data class GetPostRespDto(
  val post: Post,
  val hashTags: List<HashTag>?
)

data class GetPostReqDto(
  val hashTagId: Long?,
  val search: String?,
  override var start: Int,
  override var perPage: Int,
): PaginationDto()

data class GetPostListRespDto(
  val data: List<GetPostRespDto>,
  val total: Int
)