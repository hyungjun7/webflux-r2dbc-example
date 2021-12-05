package info.hyungjun.blogbackend.auth

import info.hyungjun.blogbackend.models.auth.PostAuthReqDTO
import info.hyungjun.blogbackend.models.user.PostUserRespDTO
import info.hyungjun.blogbackend.routes.auth.AuthHandler
import info.hyungjun.blogbackend.routes.auth.AuthRoutes
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient

@WebFluxTest
class AuthHandlerTests {
  @MockBean
  lateinit var authHandler: AuthHandler
  private lateinit var client: WebTestClient
  private val endpoint = "/auth"
  
  @BeforeEach
  fun beforeTest() {
    client = WebTestClient
      .bindToRouterFunction(AuthRoutes().authRoute(authHandler))
      .build()
  }
  
  @Test
  fun loginTest() {
    client.post()
      .uri(endpoint)
      .bodyValue(PostAuthReqDTO("string", "string"))
      .exchange()
      .expectStatus().is2xxSuccessful
      .expectBody(PostUserRespDTO::class.java)
  }
}