package info.hyungjun.blogbackend.exceptions

import org.springframework.http.HttpStatus

class NotFoundException: GlobalException(HttpStatus.NOT_FOUND, "not_found")