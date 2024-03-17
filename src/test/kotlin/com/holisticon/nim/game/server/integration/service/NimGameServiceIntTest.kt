package com.holisticon.nim.game.server.integration.service

import com.holisticon.nim.game.server.controller.NimGameController
import com.holisticon.nim.game.server.domain.GamePhase
import com.holisticon.nim.game.server.domain.NimGameState
import com.holisticon.nim.game.server.domain.PlayerType
import com.holisticon.nim.game.server.integration.TestContainer
import com.holisticon.nim.game.server.repository.NimGameRepository
import com.holisticon.nim.game.server.service.NimGameServiceImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest
import org.springframework.context.annotation.Import

import java.util.*

@DataRedisTest
@Import(NimGameController::class, TestContainer::class, NimGameServiceImpl::class)
class NimGameServiceIntTest {

    @Autowired
    private lateinit var nimGameRepository: NimGameRepository

    @Autowired
    private lateinit var nimGameService: NimGameServiceImpl

    @BeforeEach
    fun setup() {
        nimGameService = NimGameServiceImpl(nimGameRepository)
    }

    @Test
    fun `givenGameState_whenStartGame_thenReturnNewGameState`() {

        val gameState = NimGameState(UUID.randomUUID(), 3, PlayerType.HUMAN, 13, GamePhase.IN_PROGRESS, true)

        nimGameService.startGame(gameState)

        val result = nimGameRepository.findById(gameState.id.toString())
        assert(result.get() == gameState)
    }

    @Test
    fun `givenGameState_whenRestartGame_thenReturnUpdatedGameState`() {

        val gameId = UUID.randomUUID()
        val initialGameState = NimGameState(gameId, 3, PlayerType.HUMAN, 13, GamePhase.IN_PROGRESS, true)
        val finalGameState = NimGameState(gameId, 3, PlayerType.HUMAN, 13, GamePhase.PLAYER_WON, false)

        nimGameRepository.save(initialGameState)

        nimGameService.restartGame(finalGameState)

        val result = nimGameRepository.findAllById(mutableListOf(gameId.toString()))
        assert(result.toList().size == 1)
        assert(result.toList()[0].gamePhase == GamePhase.PLAYER_WON)
        Assertions.assertFalse(result.toList()[0].intelligenceMode)
    }

    @Test
    fun `givenGameState_whenPlayerMove_thenReturnUpdatedGameState`() {

        val gameId = UUID.randomUUID()
        val initialGameState = NimGameState(gameId, 3, PlayerType.HUMAN, 13, GamePhase.IN_PROGRESS, true)

        nimGameRepository.save(initialGameState)

        nimGameService.playerMove(gameId.toString(),1)

        val result = nimGameRepository.findAllById(mutableListOf(gameId.toString()))
        assert(result.toList().size == 1)
        assert(result.toList()[0].heapSize < initialGameState.heapSize)
    }

    @Test
    fun `givenGameState_whenFetchGame_thenReturnGameState`() {

        val gameId = UUID.randomUUID()
        val initialGameState = NimGameState(gameId, 3, PlayerType.HUMAN, 13, GamePhase.IN_PROGRESS, true)

        nimGameRepository.save(initialGameState)

        val result = nimGameService.fetchGameStatus(gameId.toString())
        assert(result == initialGameState)
    }

}