package info.hyungjun.blogbackend.models.user

import info.hyungjun.blogbackend.exceptions.MethodNoArgumentException
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.isEqual
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDateTime

@Repository
class UserRepository(
  private val entityTemplate: R2dbcEntityTemplate
) {
  val tableName = "User"
  
  fun insert(data: User): Mono<User> {
    return entityTemplate.insert(data)
  }
  
  fun findByEmail(email: String): Mono<User> {
    return entityTemplate.selectOne(
      Query.query(
        Criteria.where("email").isEqual(email)
      ),
      User::class.java
    )
  }
  
  fun findById(id: Long): Mono<User> {
    return entityTemplate.selectOne(
      Query.query(
        Criteria.where("id").isEqual(id)
      ),
      User::class.java
    )
  }
}