package com.holisticon.nim.game.server.service

import com.holisticon.nim.game.server.domain.GamePhase
import com.holisticon.nim.game.server.domain.NimGameState
import com.holisticon.nim.game.server.exception.EntityNotFoundException
import com.holisticon.nim.game.server.exception.InvalidParamException
import com.holisticon.nim.game.server.repository.NimGameRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class NimGameServiceImpl(
    private val nimGameRepository: NimGameRepository
) : NimGameService {

    override fun startGame(nimGameState: NimGameState): NimGameState {
        validateInitializationRules(nimGameState)
        logger.info("New Game started with gameId ${nimGameState.id}.")
        return nimGameRepository.save(nimGameState)
    }

    override fun restartGame(nimGameState: NimGameState): NimGameState  {
        validateGameStateExistence(nimGameState.id.toString())
        validateInitializationRules(nimGameState)
        logger.info("Game restarted with gameId ${nimGameState.id}.")
        return nimGameRepository.save(nimGameState)
    }

    override fun playerMove(gameId: String, move: Int): NimGameState {
        val currentGame = validateGameStateExistence(gameId)

        validateGameInProgress(currentGame)
        validateMoveParameters(currentGame, move)

        currentGame.heapSize -= move
        logger.info("PlayerMove of $move stones was preformed on GameState with gameId $gameId.")

        if (currentGame.heapSize == 0) {
            currentGame.gamePhase = GamePhase.COMPUTER_WON
            return currentGame
        }

        return when {
            currentGame.intelligenceMode -> intelligentComputerMove(currentGame)
            else -> computerMove(currentGame)
        }
    }

    fun computerMove(currentGame: NimGameState): NimGameState {
        val randomMove = Random.nextInt(1, minOf(currentGame.maxMoveAllowed, currentGame.heapSize) + 1)
        currentGame.heapSize -= randomMove

        if (currentGame.heapSize == 0) {
            currentGame.gamePhase = GamePhase.PLAYER_WON
            currentGame.heapSize = 0
        }
        logger.info("ComputerMove of $randomMove stones was preformed on GameState with gameId ${currentGame.id}.")
        return nimGameRepository.save(currentGame)
    }

    fun intelligentComputerMove(currentGame: NimGameState): NimGameState {
        // Calculate the remaining stones after the human player's move
        val remainingStones = currentGame.heapSize % (currentGame.maxMoveAllowed + 1)

        // Determine the computer's move based on the remaining stones
        val computerMove = when {
            remainingStones == 1 -> 1 // Take the last stone if only one remains
            remainingStones != 0 -> remainingStones - 1 // Take one stone each time
            currentGame.heapSize == (currentGame.maxMoveAllowed + 1) -> currentGame.heapSize - 1 // Ensure that leave one remaining
            else -> 1 // Default move: take one stone
        }

        // Update the game state with the computer's move
        currentGame.heapSize -= computerMove

        // If no stones remain, mark the game phase as PLAYER_WON
        if (currentGame.heapSize == 0) {
            currentGame.gamePhase = GamePhase.PLAYER_WON
        }

        logger.info("IntelligentComputerMove of $computerMove stones was preformed on GameState with gameId ${currentGame.id}.")
        // Save and return the updated game state
        return nimGameRepository.save(currentGame)
    }

    override fun fetchGameStatus(gameId: String): NimGameState {
        return validateGameStateExistence(gameId)
    }

    //----------------------------------------------- Utils() ------------------------------------------------------//

    override fun validateGameStateExistence(gameId: String): NimGameState {
        val gameStateOptional = nimGameRepository.findById(gameId)
        if (!gameStateOptional.isPresent) {
            logger.error("Entity Not Found with id: $gameId")
            throw EntityNotFoundException()
        }
        return gameStateOptional.get()
    }

    override fun validateInitializationRules(nimGameState: NimGameState) {

        if (nimGameState.maxMoveAllowed <= 0 || nimGameState.heapSize <= 0) {
            logger.error("Invalid parameters were sent during game initialization. maxMoveAllowed and heapSize should be greater than zero.")
            throw InvalidParamException(
                "Invalid parameters were sent during game initialization. maxMoveAllowed and heapSize should be greater than zero.")
        }

        if (nimGameState.heapSize <= nimGameState.maxMoveAllowed) {
            logger.error("Invalid parameters were sent during game initialization. heapSize should be greater than maxMoveAllowed.")
            throw InvalidParamException("Invalid parameters were sent during game initialization. heapSize should be greater than maxMoveAllowed.")
        }
    }

    override fun validateMoveParameters(game: NimGameState, move: Int) {
        if (move > game.heapSize || move > game.maxMoveAllowed || move == 0) {
            throw InvalidParamException("Invalid parameter was sent on player turn. Please fetch the game and plan your next move.")
        }
    }

    override fun validateGameInProgress(game: NimGameState) {
        if (game.gamePhase != GamePhase.IN_PROGRESS) {
            throw InvalidParamException("This game was completed and won by ${game.gamePhase}")
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NimGameServiceImpl::class.java)
    }

}