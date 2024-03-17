package com.holisticon.nim.game.server.unit.domain

import com.holisticon.nim.game.server.domain.GamePhase
import com.holisticon.nim.game.server.domain.NimGameState
import com.holisticon.nim.game.server.domain.PlayerType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class NimGameStateTest {

    //----------------------------------------------- NimGameState.toDto() ------------------------------------------------------//

    @Test
    fun `givenValidNimGameState_whenToDto_thenNimGameStateDto`() {

        val gameState = NimGameState(UUID.randomUUID(), 3, PlayerType.HUMAN, 13, GamePhase.IN_PROGRESS, true)

        val gameStateDto = gameState.toDto()

        Assertions.assertEquals(13, gameStateDto.heapSize)
        Assertions.assertEquals(3, gameStateDto.maxMoveAllowed)
        Assertions.assertEquals(GamePhase.IN_PROGRESS, gameStateDto.gamePhase)
        Assertions.assertEquals(PlayerType.HUMAN, gameStateDto.player)
    }

}