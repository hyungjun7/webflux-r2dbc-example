package info.hyungjun.blogbackend.exceptions

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Component
class GlobalErrorAttributes : DefaultErrorAttributes() {
  override fun getErrorAttributes(request: ServerRequest?, options: ErrorAttributeOptions?): Map<String, Any?>? {
    val throwable = getError(request)
    val result = mutableMapOf<String, Any?>()
    if (throwable is GlobalException) {
      val ex = getError(request) as GlobalException
      result["message"] = ex.message
      return result
    }
    result["message"] = throwable.message
    return result
  }
}

@Component
@Order(-2)
class GlobalErrorHandler(
  errorAttributes: ErrorAttributes?,
  resources: WebProperties,
  applicationContext: ApplicationContext?,
  configure: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(errorAttributes, resources.resources, applicationContext) {
  
  private val logger = LoggerFactory.getLogger(GlobalErrorHandler::class.java)
  
  init {
    super.setMessageWriters(configure.writers)
    super.setMessageReaders(configure.readers)
  }
  
  override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse> {
    return RouterFunctions.route(
      RequestPredicates.all(), this::renderErrorResponse)
  }
  
  private fun renderErrorResponse(
    request: ServerRequest
  ): Mono<ServerResponse> {
    val throwable = getError(request)
    val result = mutableMapOf<String, Any>()
    if (throwable is GlobalException) {
      result["message"] = throwable.message
      logger.error(
        "RESP [{}]: {} {} {}",
        request.exchange().request.id,
        throwable.status,
        throwable.message,
        throwable.stackTraceToString()
      )
      return ServerResponse.status(throwable.status).bodyValue(result)
    }
    logger.error(
      "RESP [{}]: {} {} {}",
      request.exchange().request.id,
      500,
      throwable.message,
      throwable.stackTraceToString()
    )
    result["message"] = "INTERNAL_SERVER_ERROR"
    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(result)
  }
}