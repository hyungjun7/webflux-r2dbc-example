package info.hyungjun.blogbackend.auth

import info.hyungjun.blogbackend.models.auth.PostAuthReqDTO
import info.hyungjun.blogbackend.models.user.PostUserRespDTO
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.test.web.reactive.server.WebTestClient

class AuthHandlerTests {
  private val client: WebTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build()
  
  @Test
  fun loginTest() {
    client.post()
      .uri("/api/blog/auth")
      .header(CONTENT_TYPE, "application/json")
      .bodyValue(PostAuthReqDTO("string", "string"))
      .exchange()
      .expectStatus().is2xxSuccessful
      .expectBody(PostUserRespDTO::class.java)
  }
}