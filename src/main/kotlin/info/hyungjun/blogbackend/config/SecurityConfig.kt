package info.hyungjun.blogbackend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
  @Bean
  fun securityFilterChain(
    http: ServerHttpSecurity,
    authenticationManager: JwtAuthenticationManager,
    authenticationConverter: JwtAuthenticationConverter
  ): SecurityWebFilterChain {
    val authenticationWebFilter = AuthenticationWebFilter(authenticationManager)
    authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter)
    http
      .csrf()
        .disable()
      .logout()
        .disable()
      .formLogin()
        .disable()
      .httpBasic()
      .authenticationEntryPoint(HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
      .authenticationManager(authenticationManager)
      
      http
      .authorizeExchange()
        .pathMatchers(HttpMethod.GET, "/user")
          .authenticated()
        .pathMatchers("/**")
          .permitAll()
      .and()
        .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .logout()
          .disable()
        .csrf()
        .disable()
        .formLogin()
        .disable()
      .httpBasic()
        .authenticationEntryPoint(HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
        .authenticationManager(authenticationManager)
    
    return http.build()
  }
}