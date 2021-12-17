package info.hyungjun.blogbackend.exceptions

import org.springframework.http.HttpStatus

class DuplicateException: GlobalException(HttpStatus.CONFLICT, "already_in_use")