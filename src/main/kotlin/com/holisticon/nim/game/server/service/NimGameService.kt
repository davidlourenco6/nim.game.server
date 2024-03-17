package com.holisticon.nim.game.server.service

import com.holisticon.nim.game.server.domain.NimGameState
import com.holisticon.nim.game.server.exception.EntityNotFoundException
import com.holisticon.nim.game.server.exception.InvalidParamException

interface NimGameService {

    /**
     * Starts a new game instance based on specified parameters.
     *
     * @param [nimGameState] that represents the desired initial game state.
     * @return A [NimGameState] that represents the initial state after initialization.
     * @throws InvalidParamException if the `move` parameter is invalid
     * (e.g., greater than the current heap size, greater than the maximum
     * allowed move, or zero).
     */
    fun startGame(nimGameState: NimGameState): NimGameState

    /**
     * Restarts a new game instance based on specified parameters.
     *
     * @param [nimGameState] that represents the desired initial game state.
     * @return A [NimGameState] that represents the initial state after reinitialization.
     * @throws InvalidParamException if the `move` parameter is invalid
     * (e.g., greater than the current heap size, greater than the maximum
     * allowed move, or zero).
     */
    fun restartGame(nimGameState: NimGameState): NimGameState

    /**
     * Perform game move based on specified parameters.
     *
     * @param [gameId] that represents a game state instance`id`.
     * @param [move] that represents a desired number of moves.
     * @return A [NimGameState] that represents the current game state after move.
     * @throws EntityNotFoundException If the provided `id` does not correspond to an
     * existing game.
     * @throws InvalidParamException if the `move` parameter is invalid
     * (e.g., greater than the current heap size, greater than the maximum
     * allowed move, or zero).
     */
    fun playerMove(gameId: String, move: Int): NimGameState

    /**
     * Fetch a game instance by gameId.
     *
     * @param [gameId] that represents a game state instance`id`.
     * @throws EntityNotFoundException If the provided `id` does not correspond to an
     * existing game.
     */
    fun fetchGameStatus(gameId: String): NimGameState

    /**
     * Validate game state existence by id.
     *
     * @param [gameId] That represents the desired game state.
     * @return [NimGameState] That represents the validated game state.
     * @throws EntityNotFoundException If provided parameters are not aligned with game rules
     */
    fun validateGameStateExistence(gameId: String): NimGameState

    /**
     * Validate if basic game rules are respected.
     * (maxMoveAllowed > 0, heapSize > 0, heapSize > maxMoveAllowed)
     *
     * @param [nimGameState] That represents the desired game state.
     * @throws InvalidParamException If provided parameters are not aligned with game rules
     */
    fun validateInitializationRules(nimGameState: NimGameState)

    /**
     * Validates move parameters.
     *
     * @param game The Nim game state.
     * @param move The move to be validated.
     * @throws InvalidParamException If the move parameters are invalid.
     */
    fun validateMoveParameters(game: NimGameState, move: Int)

    /**
     * Validates whether the provided game is not in a final state.
     *
     * @param game The Nim game state to be validated.
     * @throws InvalidParamException If the game is not in progress.
     */
    fun validateGameInProgress(game: NimGameState)
}