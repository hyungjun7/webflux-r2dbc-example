package info.hyungjun.blogbackend.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

open class GlobalException : ResponseStatusException {
  constructor(status: HttpStatus?) : super(status!!) {}
  constructor(status: HttpStatus?, reason: String?) : super(status!!, reason) {}
}