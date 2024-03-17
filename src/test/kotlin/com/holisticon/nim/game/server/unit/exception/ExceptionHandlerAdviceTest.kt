package com.holisticon.nim.game.server.unit.exception

import com.holisticon.nim.game.server.exception.EntityNotFoundException
import com.holisticon.nim.game.server.exception.ExceptionHandlerAdvice
import com.holisticon.nim.game.server.exception.InvalidParamException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus

@ExtendWith(MockitoExtension::class)
class ExceptionHandlerAdviceTest {

    @InjectMocks
    lateinit var exceptionHandlerAdvice: ExceptionHandlerAdvice

    //----------------------------------------------- handleEntityNotFoundException() ------------------------------------------------------//

    @Test
    fun `givenEntityNotFoundExceptionWhenHandleEntityNotFoundExceptionThenBadRequest`() {

        val response = exceptionHandlerAdvice.handleEntityNotFoundException(EntityNotFoundException())

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        Assertions.assertEquals("EntityNotFoundException: Entity not found", response.body)

    }

    //----------------------------------------------- handleInvalidParamException() ------------------------------------------------------//

    @Test
    fun `givenInvalidParamExceptionWhenHandleInvalidParamExceptionThenBadRequest`() {
        val response = exceptionHandlerAdvice.handleInvalidParamException(InvalidParamException("InvalidParamsException"))

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        Assertions.assertEquals("InvalidParamException: InvalidParamsException", response.body)
    }
}