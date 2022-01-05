package info.hyungjun.blogbackend.models.postHashTag

import info.hyungjun.blogbackend.models.hashTag.HashTag
import info.hyungjun.blogbackend.models.hashTag.HashTagRepository
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class PostHashTagRepository(
  private val databaseClient: DatabaseClient,
  private val hashTagRepository: HashTagRepository
) {
  private val tableName = "`Post-HashTag`"
  fun insertAll(data: List<PostHashTagCreateDao>): Mono<Int> {
    val insertData = mutableListOf<Array<Long>>()
    data.forEach {
      insertData.add(arrayOf(it.postId, it.hashId))
    }
    return databaseClient.sql("INSERT INTO $tableName (postId, hashTagId) VALUES :item")
      .bind("item", insertData)
      .fetch()
      .rowsUpdated()
  }
  
  fun findAllByPostId(postId: Long): Flux<HashTag> {
    return databaseClient.sql(
      "SELECT h.* FROM $tableName t " +
        "JOIN ${hashTagRepository.tableName} h ON t.hashTagId = h.id " +
        "AND t.postId = :postId"
    )
      .bind("postId", postId)
      .map { row ->
        HashTag(id = row["id"] as Long, name = row["name"] as String)
      }
      .all()
  }
}