package info.hyungjun.blogbackend.modules.loggers

import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.channels.Channels
import java.nio.charset.Charset

class ResponseLogger(
  private val exchange: ServerWebExchange,
  private val startTime: Long
) :
  ServerHttpResponseDecorator(exchange.response) {
  
  private val logger = LoggerFactory.getLogger(RequestLogger::class.java)
  
  override fun writeWith(body: Publisher<out DataBuffer?>): Mono<Void> {
    val buffer: Flux<DataBuffer> = Flux.from(body)
    return super.writeWith(buffer.doOnNext {
      val baos = ByteArrayOutputStream()
      try {
        Channels.newChannel(baos).write(it.asByteBuffer().asReadOnlyBuffer())
        val bodyRes = baos.toByteArray().toString(Charset.forName("UTF-8"))
        logger.info(
          "RESP [{}] {}ms: {} {}",
          exchange.request.id,
          System.currentTimeMillis() - startTime,
          statusCode.value(),
          bodyRes
        )
      } catch (e: Exception) {
        logger.error(e.stackTraceToString())
      } finally {
        try {
          baos.close()
        } catch (e: IOException) {
          logger.error(e.stackTraceToString())
        }
      }
    })
  }
}