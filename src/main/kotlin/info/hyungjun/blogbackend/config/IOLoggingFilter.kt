package info.hyungjun.blogbackend.config

import info.hyungjun.blogbackend.modules.loggers.RequestLogger
import info.hyungjun.blogbackend.modules.loggers.ResponseLogger
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebExchangeDecorator
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Configuration
class IOLoggingFilter: WebFilter {
  override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
    val startTime = System.currentTimeMillis()
    
    val exchangeDecorator = object : ServerWebExchangeDecorator(exchange) {
      override fun getRequest(): ServerHttpRequest {
        return RequestLogger(super.getRequest())
      }
    
      override fun getResponse(): ServerHttpResponse {
        return ResponseLogger(super.getDelegate(), startTime)
      }
    }
    return chain.filter(exchangeDecorator)
  }
}