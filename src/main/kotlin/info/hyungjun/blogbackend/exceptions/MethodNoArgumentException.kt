package info.hyungjun.blogbackend.exceptions

import org.springframework.http.HttpStatus

class MethodNoArgumentException: GlobalException(HttpStatus.INTERNAL_SERVER_ERROR, "no arguments in query method")