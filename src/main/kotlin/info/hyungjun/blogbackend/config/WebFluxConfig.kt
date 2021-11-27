package info.hyungjun.blogbackend.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.PathMatchConfigurer
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
class WebFluxConfig: WebFluxConfigurer {
  override fun configurePathMatching(configurer: PathMatchConfigurer) {
    configurer.addPathPrefix("/api/blog") {true}
  }
}