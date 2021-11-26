package info.hyungjun.blogbackend.models.user

import info.hyungjun.blogbackend.common.DuplicateException
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository: CoroutineCrudRepository<User, Int> {

}