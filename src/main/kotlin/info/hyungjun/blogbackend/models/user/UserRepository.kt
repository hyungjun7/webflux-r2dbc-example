package info.hyungjun.blogbackend.models.user

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CoroutineCrudRepository<User, Int> {
  
  @Query("SELECT id, email, created_at FROM User")
  suspend fun findUser(): List<FindUser>
  
  @Query("SELECT * FROM User WHERE email = :email")
  suspend fun findUserByEmail(email: String): User?
}