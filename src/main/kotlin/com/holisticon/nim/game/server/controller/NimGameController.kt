package com.holisticon.nim.game.server.controller

import com.holisticon.nim.game.server.controller.dto.NimGameResetStateDto
import com.holisticon.nim.game.server.controller.dto.NimGameStateDto

import com.holisticon.nim.game.server.service.NimGameService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/nim-game/")
class NimGameController(private var nimGameDefaultServiceImpl: NimGameService) {

    @PostMapping("v1/start")
    fun startGame(
        @RequestBody initialNimGameStateDto: NimGameStateDto
    ): NimGameStateDto
    {
        val initialNimGameState = initialNimGameStateDto.toEntity()
        val startedNimGameState = nimGameDefaultServiceImpl.startGame(initialNimGameState)
        return startedNimGameState.toDto()
    }

    @PostMapping("v1/restart")
    fun restartGame(
        @RequestBody nimGameResetStateDto: NimGameResetStateDto
    ): NimGameStateDto
    {
        val nimGameResetState = nimGameResetStateDto.toEntity()
        val resetNimGameState = nimGameDefaultServiceImpl.restartGame(nimGameResetState)
        return resetNimGameState.toDto()
    }

    @PostMapping("v1/play/{id}/{move}")
    fun playerMove(
        @PathVariable id: String,
        @PathVariable move: Int
    ): NimGameStateDto
    {
        val startedNimGameState = nimGameDefaultServiceImpl.playerMove(id, move)
        return startedNimGameState.toDto()
    }

    @GetMapping("v1/fetch/{id}")
    fun fetchGame(
        @PathVariable id: String
    ): NimGameStateDto
    {
        val resetNimGameState = nimGameDefaultServiceImpl.fetchGameStatus(id)
        return resetNimGameState.toDto()
    }

}