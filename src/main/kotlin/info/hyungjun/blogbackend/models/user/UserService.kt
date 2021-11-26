package info.hyungjun.blogbackend.models.user

import info.hyungjun.blogbackend.common.DuplicateException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
  val userRepository: UserRepository
) {
  
  @Throws(DuplicateException::class)
  suspend fun createUser(data: UserCreate): User {
    return userRepository.save(User(0, data.email, data.password, "", UserRoles.Admin, Date().toString()))
  }
  
  suspend fun findUser(): List<User> {
    return userRepository.findAll().toList()
  }
}