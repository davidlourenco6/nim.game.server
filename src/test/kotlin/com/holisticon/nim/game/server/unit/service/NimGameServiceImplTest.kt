package com.holisticon.nim.game.server.unit.service
import com.holisticon.nim.game.server.domain.GamePhase
import com.holisticon.nim.game.server.domain.NimGameState
import com.holisticon.nim.game.server.domain.PlayerType
import com.holisticon.nim.game.server.exception.EntityNotFoundException
import com.holisticon.nim.game.server.exception.InvalidParamException
import com.holisticon.nim.game.server.repository.NimGameRepository
import com.holisticon.nim.game.server.service.NimGameServiceImpl
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class NimGameServiceImplTest {

    private lateinit var nimGameRepository: NimGameRepository
    private lateinit var nimGameService: NimGameServiceImpl

    @BeforeEach
    fun setUp() {
        nimGameRepository = mockk()
        nimGameService = NimGameServiceImpl(nimGameRepository)
    }

    //----------------------------------------------- startGame() ------------------------------------------------------//

    @Test
    fun `givenValidNimGameStateWhenStartGameThenGameStateCreated`() {
        val gameState = NimGameState(UUID.randomUUID(), 3, PlayerType.HUMAN, 13 ,GamePhase.IN_PROGRESS, true)
        every { nimGameRepository.save(any()) } returns gameState

        val result = nimGameService.startGame(gameState)

        assertEquals(gameState, result)
    }

    //----------------------------------------------- restartGame() ------------------------------------------------------//

    @Test
    fun `givenValidNimGameStateWhenRestartGameThenGameStateUpdated`() {
        val gameState = NimGameState(UUID.randomUUID(), 3, PlayerType.HUMAN, 13 ,GamePhase.IN_PROGRESS, true)

        every { nimGameRepository.findById(any()) } returns Optional.of(gameState)
        every { nimGameRepository.save(any()) } returns gameState

        val result = nimGameService.restartGame(gameState)

        assertEquals(gameState, result)
    }

    @Test
    fun `givenInValidNimGameStateWhenRestartGameThenEntityNotFoundException`() {
        val gameState = NimGameState(UUID.randomUUID(), 3, PlayerType.HUMAN, 13 ,GamePhase.IN_PROGRESS, true)

        every { nimGameRepository.findById(any()) } returns Optional.empty()

        assertThrows(EntityNotFoundException::class.java) {
            nimGameService.restartGame(gameState)
        }
    }

    //----------------------------------------------- playerMove() ------------------------------------------------------//

    @Test
    fun `givenValidIdAndMoveWhenPlayerMoveThenGameStateUpdated`() {
        val gameState = NimGameState(UUID.randomUUID(), 1, PlayerType.HUMAN, 2 ,GamePhase.IN_PROGRESS, true)
        val updatedGameState = NimGameState(UUID.randomUUID(), 1, PlayerType.HUMAN, 0 ,GamePhase.IN_PROGRESS, true)
        every { nimGameRepository.findById(gameState.id.toString()) } returns Optional.of(gameState)

        every { nimGameRepository.save(any()) } returns updatedGameState

        val result = nimGameService.playerMove(gameState.id.toString(), 1)

        assertEquals(0, result.heapSize)
    }

    @Test
    fun `givenInvalidIdWhenPlayerMoveThenEntityNotFoundException`() {
        val gameState = NimGameState(UUID.randomUUID(), 3, PlayerType.HUMAN, 13 ,GamePhase.IN_PROGRESS, true)
        every { nimGameRepository.findById(gameState.id.toString()) } returns Optional.empty()

        assertThrows(EntityNotFoundException::class.java) {
            nimGameService.playerMove(gameState.id.toString(), 1)
        }
    }

    @Test
    fun `givenInvalidMoveWhenPlayerMoveThenEntityNotFoundException`() {
        val gameState = NimGameState(UUID.randomUUID(), 1, PlayerType.HUMAN, 2 ,GamePhase.IN_PROGRESS, true)
        every { nimGameRepository.findById(gameState.id.toString()) } returns Optional.of(gameState)

        assertThrows(InvalidParamException::class.java) {
            nimGameService.playerMove(gameState.id.toString(), 99)
        }

    }

    @Test
    fun `givenLastMoveWhenPlayerMoveThenGamePhaseChange`() {
        val gameState = NimGameState(UUID.randomUUID(), 1, PlayerType.HUMAN, 1 ,GamePhase.IN_PROGRESS, true)
        every { nimGameRepository.findById(gameState.id.toString()) } returns Optional.of(gameState)

        val result = nimGameService.playerMove(gameState.id.toString(), 1)

        assertEquals(GamePhase.COMPUTER_WON, result.gamePhase)
        assertEquals(0, result.heapSize)
    }

    @Test
    fun `givenCurrentMoveWhenPlayerMoveThenNimGameState`() {
        val gameState = NimGameState(UUID.randomUUID(), 3, PlayerType.HUMAN, 13 ,GamePhase.IN_PROGRESS, true)
        every { nimGameRepository.findById(gameState.id.toString()) } returns Optional.of(gameState)
        every { nimGameRepository.save(gameState) } returns gameState

        val result = nimGameService.playerMove(gameState.id.toString(), 3)

        assertEquals(GamePhase.IN_PROGRESS, result.gamePhase)
    }

    @Test
    fun `givenCurrentMoveWhenPlayerMoveThenNimGameState1`() {
        val gameState = NimGameState(UUID.randomUUID(), 3, PlayerType.HUMAN, 13 ,GamePhase.IN_PROGRESS, false)
        every { nimGameRepository.findById(gameState.id.toString()) } returns Optional.of(gameState)
        every { nimGameRepository.save(gameState) } returns gameState

        val result = nimGameService.playerMove(gameState.id.toString(), 3)

        verify { nimGameService.computerMove(gameState) }

        assertEquals(GamePhase.IN_PROGRESS, result.gamePhase)
    }

    //----------------------------------------------- computerMove() ------------------------------------------------------//

    @Test
    fun `givenFinalMatchOnLosePosition_whenComputerMove_thenUpdateGamePhase`() {
        val gameState = NimGameState(UUID.randomUUID(), 1, PlayerType.HUMAN, 1 ,GamePhase.IN_PROGRESS, true)
        every { nimGameRepository.save(gameState) } returns gameState

        val result = nimGameService.computerMove(gameState)

        assertEquals(GamePhase.PLAYER_WON, result.gamePhase)
        assertEquals(0, result.heapSize)
    }

    @Test
    fun `givenMatchOnCurrentPosition_whenComputerMove_thenNimGameState`() {
        val gameState = NimGameState(UUID.randomUUID(), 3, PlayerType.HUMAN, 13 ,GamePhase.IN_PROGRESS, true)
        every { nimGameRepository.save(gameState) } returns gameState

        val result = nimGameService.computerMove(gameState)

        assertEquals(GamePhase.IN_PROGRESS, result.gamePhase)
    }

    //----------------------------------------------- intelligentComputerMove() -----------------------------------------//


    @Test
    fun `givenFinalMatchOnWinningPosition_whenIntelligentComputerMove_thenLeaveOne`() {
        val gameState = NimGameState(UUID.randomUUID(), 3, PlayerType.HUMAN, 4 ,GamePhase.IN_PROGRESS, true)
        every { nimGameRepository.save(gameState) } returns gameState

        val result = nimGameService.intelligentComputerMove(gameState)

        assertEquals(1, result.heapSize)
    }

    @Test
    fun `givenFinalMatchOnLosePosition_whenIntelligentComputerMove_thenUpdateGamePhase`() {
        val gameState = NimGameState(UUID.randomUUID(), 1, PlayerType.HUMAN, 1 ,GamePhase.IN_PROGRESS, true)
        every { nimGameRepository.save(gameState) } returns gameState

        val result = nimGameService.intelligentComputerMove(gameState)

        assertEquals(GamePhase.PLAYER_WON, result.gamePhase)
        assertEquals(0, result.heapSize)
    }

    @Test
    fun `givenMatchOnCurrentPosition_whenIntelligentComputerMove_thenNimGameState`() {
        val gameState = NimGameState(UUID.randomUUID(), 3, PlayerType.HUMAN, 13 ,GamePhase.IN_PROGRESS, true)
        every { nimGameRepository.save(gameState) } returns gameState

        val result = nimGameService.computerMove(gameState)

        assertEquals(GamePhase.IN_PROGRESS, result.gamePhase)
    }

    //----------------------------------------------- fetchGame() ------------------------------------------------------//

    @Test
    fun `givenValidId_whenFetchGameStatus_thenNimGameState`() {
        val gameState = NimGameState(UUID.randomUUID(), 3, PlayerType.HUMAN, 13 ,GamePhase.IN_PROGRESS, true)
        every { nimGameRepository.findById(gameState.id.toString()) } returns Optional.of(gameState)

        val result = nimGameService.fetchGameStatus(gameState.id.toString())

        assertEquals(gameState, result)
    }

    @Test
    fun `givenInvalidId_whenFetchGameStatus_thenEntityNotFoundException`() {
        val gameId = UUID.randomUUID().toString()
        every { nimGameRepository.findById(gameId) } returns Optional.empty()
        assertThrows(EntityNotFoundException::class.java) {
            nimGameService.fetchGameStatus(gameId)
        }
    }

    //----------------------------------------------- validateGameStateExistence() ------------------------------------------------------//

    @Test
    fun `givenInvalidId_whenvalidateGameStateExistence_thenEntityNotFoundException`() {
        every { nimGameRepository.findById(any()) } returns Optional.empty()

        assertThrows(EntityNotFoundException::class.java) {
            nimGameService.validateGameStateExistence("nonexistent-id")
        }
    }

    @Test
    fun `givenInvalidId_whenvalidateGameStateExistence_thenValidNimGameState`() {
        val gameState = NimGameState(UUID.randomUUID(), 3, PlayerType.HUMAN, 13 ,GamePhase.IN_PROGRESS, true)
        every { nimGameRepository.findById(gameState.id.toString()) } returns Optional.of(gameState)

        val result = nimGameService.validateGameStateExistence(gameState.id.toString())

        assertEquals(gameState, result)
    }

    //----------------------------------------------- validateGameStateExistence() ------------------------------------------------------//

    @Test
    fun `givenInvalidMaxMoveAllowed_whenValidateInitializationRules_thenInvalidParamException`() {

        val invalidGameState = NimGameState(UUID.randomUUID(), -1, PlayerType.HUMAN, 13, GamePhase.IN_PROGRESS, true)

        assertThrows(InvalidParamException::class.java) {
            nimGameService.validateInitializationRules(invalidGameState)
        }
    }

    @Test
    fun `givenMaxMoveAllowedBiggerThanHeapSize_whenValidateInitializationRules_thenInvalidParamException`() {

        val invalidGameState = NimGameState(UUID.randomUUID(), 11, PlayerType.HUMAN, 10, GamePhase.IN_PROGRESS, true)

        assertThrows(InvalidParamException::class.java) {
            nimGameService.validateInitializationRules(invalidGameState)
        }
    }

    //----------------------------------------------- validateMoveParameters() ------------------------------------------------------//

    @Test
    fun `givenInvalidGameMove_whenValidateMoveParameters_thenThrowInvalidParamException`() {

        val game = NimGameState(UUID.randomUUID(), 10, PlayerType.HUMAN, 10, GamePhase.IN_PROGRESS, true)

        assertThrows(InvalidParamException::class.java) {
            nimGameService.validateMoveParameters(game, 11)
        }
    }

    @Test
    fun `givenGameInFinalState_whenValidateGameInProgress_thenThrow InvalidParamException`() {
        val game = NimGameState(UUID.randomUUID(), 10, PlayerType.HUMAN, 5, GamePhase.COMPUTER_WON, true)

        assertThrows(InvalidParamException::class.java) {
            nimGameService.validateGameInProgress(game)
        }
    }
}
