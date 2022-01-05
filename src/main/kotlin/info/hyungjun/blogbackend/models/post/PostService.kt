package info.hyungjun.blogbackend.models.post

import info.hyungjun.blogbackend.models.hashTag.HashTag
import info.hyungjun.blogbackend.models.hashTag.HashTagRepository
import info.hyungjun.blogbackend.models.postHashTag.PostHashTagCreateDao
import info.hyungjun.blogbackend.models.postHashTag.PostHashTagRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.kotlin.adapter.rxjava.toFlowable
import java.time.LocalDateTime

@Service
class PostService(
  private val postRepository: PostRepository,
  private val hashTagRepository: HashTagRepository,
  private val postHashTagRepository: PostHashTagRepository
) {
  
  @Transactional
  suspend fun createPost(data: PostPostWithUserIdDto): GetPostRespDto {
    val post = postRepository.insert(
      Post(id = null, title = data.title, content = data.content, userId = data.userId, createdAt = LocalDateTime.now())
    ).awaitSingle()
    val hashTags = mutableListOf<HashTag>()
    val ids = mutableListOf<PostHashTagCreateDao>()
    data.hashTags.forEach {
      hashTagRepository.insert(
        HashTag(id = null, name = it.name)
      ).awaitSingleOrNull()
      val hashTag = hashTagRepository.findByName(it.name).awaitSingle()
      hashTags.add(hashTag)
      ids.add(PostHashTagCreateDao(postId = post.id!!, hashId = hashTag.id!!))
    }
    postHashTagRepository.insertAll(ids).awaitSingle()
    return GetPostRespDto(post = post, hashTags = hashTags)
  }
  
  suspend fun findAll(options: GetPostReqDto): GetPostListRespDto {
    val data = mutableListOf<GetPostRespDto>()
    postRepository.findAll(options).collectList().awaitSingle().forEach {
      data.add(
        GetPostRespDto(post = it, hashTags = postHashTagRepository.findAllByPostId(it.id!!).collectList().awaitSingle())
      )
    }
    return GetPostListRespDto(
      data = data,
      total = postRepository.count().awaitSingle().toInt()
    )
  }
}