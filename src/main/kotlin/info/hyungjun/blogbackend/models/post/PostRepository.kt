package info.hyungjun.blogbackend.models.post

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.sql.SqlIdentifier
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Repository
class PostRepository(
  private val entityTemplate: R2dbcEntityTemplate
) {
  fun insert(data: Post): Mono<Post> {
    return entityTemplate.insert(data)
  }
  
  fun count(): Mono<Long> {
    return entityTemplate.count(
      Query.query(Criteria.empty()),
      Post::class.java
    )
  }
  
  fun findAll(options: GetPostReqDto): Flux<Post> {
    return entityTemplate.select(
      Query.empty().limit(options.perPage).offset(options.start.toLong()),
      Post::class.java
    )
  }
}