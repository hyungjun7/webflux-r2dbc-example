package info.hyungjun.blogbackend.auth

import info.hyungjun.blogbackend.models.auth.PostAuthReqDTO
import info.hyungjun.blogbackend.models.user.PostUserRespDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.test.web.reactive.server.WebTestClient

class AuthHandlerTests(
  private val client: WebTestClient
) {
  
  fun loginTest(): String {
    val userInfo = PostAuthReqDTO("string", "string")
    println("loginTest: $userInfo")
    return client.post()
      .uri("/api/blog/auth")
      .header(CONTENT_TYPE, "application/json")
      .bodyValue(userInfo)
      .exchange()
      .expectStatus().isOk
      .expectBody(PostUserRespDTO::class.java)
      .returnResult()
      .responseBody
      ?.accessToken ?: ""
  }
}