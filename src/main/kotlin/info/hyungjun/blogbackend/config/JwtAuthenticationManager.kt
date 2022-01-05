package info.hyungjun.blogbackend.config

import info.hyungjun.blogbackend.exceptions.GlobalException
import info.hyungjun.blogbackend.modules.JwtModule
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationManager(
  val jwt: JwtModule
): ReactiveAuthenticationManager {
  override fun authenticate(authentication: Authentication): Mono<Authentication> {
    return Mono.just(authentication)
      .flatMap { Mono.justOrEmpty(jwt.validateJwt(it.credentials as String)) }
      .onErrorResume { throw GlobalException(HttpStatus.UNAUTHORIZED, "invalid_token") }
      .flatMap {
        Mono.just(UsernamePasswordAuthenticationToken(
          it.body.subject,
          authentication.credentials as String,
          mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
        ))
      }
  }
}