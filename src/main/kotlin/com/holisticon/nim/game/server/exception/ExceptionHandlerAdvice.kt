package com.holisticon.nim.game.server.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandlerAdvice {
    @ExceptionHandler
    fun handleEntityNotFoundException(ex: EntityNotFoundException) =
        ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler
    fun handleInvalidParamException(ex: InvalidParamException) =
        ResponseEntity(ex.message, HttpStatus.BAD_REQUEST)
}