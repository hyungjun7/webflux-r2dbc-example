package info.hyungjun.blogbackend.exceptions

import org.springframework.http.HttpStatus

class WrongPasswordException: GlobalException(HttpStatus.CONFLICT, "wrong_password")