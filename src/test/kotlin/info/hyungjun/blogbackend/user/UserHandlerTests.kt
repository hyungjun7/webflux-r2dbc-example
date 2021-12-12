package info.hyungjun.blogbackend.user

import info.hyungjun.blogbackend.models.auth.PostAuthReqDTO
import info.hyungjun.blogbackend.models.user.GetUserRespDTO
import org.springframework.test.web.reactive.server.WebTestClient

class UserHandlerTests(
  private val client: WebTestClient,
  private val accessToken: String
) {
  
  fun getUserInfo() {
    println("getUserInfoTest: $accessToken")
    client.get()
      .uri("/api/blog/user")
      .header("authorization", accessToken)
      .exchange()
      .expectStatus().isOk
      .expectBody(GetUserRespDTO::class.java)
  }
}