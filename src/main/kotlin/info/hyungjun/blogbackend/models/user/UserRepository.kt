package info.hyungjun.blogbackend.models.user

import info.hyungjun.blogbackend.exceptions.MethodNoArgumentException
import info.hyungjun.blogbackend.modules.db.DBConnection
import org.springframework.stereotype.Repository

@Repository
class UserRepository {
  
  suspend fun post(data: CreateUserDao): MutableMap<String, Any> {
    return DBConnection.getInstance().insert(
      q = "INSERT INTO $TABLE_NAME (??) VALUES (?)",
      value = data,
      connection = null
    )
  }
  
  suspend fun findOne(data: FindUserDao): UserDao? {
    val where = mutableListOf<String>()
    if (data.email != null) {
      where.add("email = '${data.email}'")
    } else if (data.id !== null) {
      where.add("id = ${data.id}")
    } else {
      throw MethodNoArgumentException()
    }
    val row = DBConnection.getInstance().selectOne(
      q = "SELECT * FROM $TABLE_NAME ${if (where.size > 0) "WHERE ${where.joinToString()}" else ""}",
      values = listOf(),
      connection = null
    )
    return UserDao(row)
  }
  
  companion object {
    const val TABLE_NAME = "User"
  }
}