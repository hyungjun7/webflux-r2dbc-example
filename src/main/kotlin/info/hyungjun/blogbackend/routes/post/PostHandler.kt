package info.hyungjun.blogbackend.routes.post

import info.hyungjun.blogbackend.models.post.GetPostReqDto
import info.hyungjun.blogbackend.models.post.PostPostReqDto
import info.hyungjun.blogbackend.models.post.PostPostWithUserIdDto
import info.hyungjun.blogbackend.models.post.PostService
import kotlinx.coroutines.reactive.awaitFirst
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*

@Component
class PostHandler(
  private val postService: PostService
) {
  suspend fun postPost(req: ServerRequest): ServerResponse {
    val data = req.awaitBody<PostPostReqDto>()
    val userId = req.principal().awaitFirst().name
    val post = postService.createPost(
      PostPostWithUserIdDto(
        title = data.title,
        content = data.content,
        userId = userId.toLong(),
        hashTags = data.hashTags
      )
    )
    return ServerResponse.status(HttpStatus.CREATED).bodyValueAndAwait(post)
  }
  
  suspend fun getPosts(req: ServerRequest): ServerResponse {
    val options = req.queryParams()
    val data = postService.findAll(
      GetPostReqDto(
        start = options.getFirst("start")!!.toInt(),
        perPage = options.getFirst("perPage")!!.toInt(),
        search = options.getFirst("search"),
        hashTagId = options.getFirst("hashTagId") as Long?
      )
    )
    return ServerResponse.ok().bodyValueAndAwait(data)
  }
}