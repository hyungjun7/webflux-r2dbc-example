package info.hyungjun.blogbackend

import info.hyungjun.blogbackend.auth.AuthHandlerTests
import info.hyungjun.blogbackend.user.UserHandlerTests
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
class BlogBackendApplicationTests {
  
  private val client: WebTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build()
  
  private var accessToken: String = AuthHandlerTests(client).loginTest()
  
  @Test
  fun userTests() {
    UserHandlerTests(client, accessToken).getUserInfo()
  }
  
}
