package info.hyungjun.blogbackend.modules.loggers

import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import reactor.core.publisher.Flux
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.channels.Channels
import java.nio.charset.Charset


class RequestLogger(delegate: ServerHttpRequest) : ServerHttpRequestDecorator(delegate) {
  
  private val logger = LoggerFactory.getLogger(RequestLogger::class.java)
  
  override fun getBody(): Flux<DataBuffer> {
    val baos = ByteArrayOutputStream()
    return super.getBody().doOnNext {
      try {
        Channels.newChannel(baos).write(it.asByteBuffer().asReadOnlyBuffer())
        val body = baos.toByteArray().toString(Charset.forName("UTF-8"))
        logger.info(
          "REQ [{}]: [{}] {} {}",
          delegate.id,
          delegate.method,
          delegate.uri,
          body
        )
      } catch (e: IOException) {
        logger.error(e.stackTraceToString())
      } finally {
        try {
          baos.close()
        } catch (e: IOException) {
          logger.error(e.stackTraceToString())
        }
      }
    }
  }
}