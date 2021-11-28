package info.hyungjun.blogbackend.config

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationConverter: ServerAuthenticationConverter {
  override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
    return Mono.justOrEmpty(exchange)
      .flatMap { Mono.justOrEmpty(it.request.headers["authorization"]) }
      .filter { it.isNotEmpty() }
      .map {
        val token = ((it as List<*>)[0] as String).replace("Bearer ", "")
        UsernamePasswordAuthenticationToken(token, token)
      }
  }
}