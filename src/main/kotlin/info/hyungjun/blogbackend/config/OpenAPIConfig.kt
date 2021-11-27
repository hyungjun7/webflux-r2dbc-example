package info.hyungjun.blogbackend.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(info = Info(
  title = "blog-backend",
  description = "hyungjun의 블로그 swagger 문서입니다.\n" + "http://localhost:8080/api/blog/swagger-ui.html",
  version = "0.1.0")
)
@SecurityScheme(
  name = "bearerAuth",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  scheme = "bearer"
)
class OpenAPIConfig