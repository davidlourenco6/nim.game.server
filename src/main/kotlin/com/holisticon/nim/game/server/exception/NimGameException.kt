package com.holisticon.nim.game.server.exception

sealed class NimGameException(
    message: String,
) : Exception(message)

class EntityNotFoundException() :
    NimGameException("EntityNotFoundException: Entity not found")

class InvalidParamException(message: String):
    NimGameException("InvalidParamException: $message")