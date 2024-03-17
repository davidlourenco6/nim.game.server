package com.holisticon.nim.game.server.unit.controller.dto

import com.holisticon.nim.game.server.controller.dto.NimGameStateDto
import com.holisticon.nim.game.server.domain.GamePhase
import com.holisticon.nim.game.server.domain.PlayerType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class NimGameStateDtoTest {

    //----------------------------------------------- NimGameStateDto.toEntity() ------------------------------------------------------//

    @Test
    fun `givenValidNimGameStateDto_whenToEntity_thenNimGameState`() {
        val maxMoveAllowed = 3
        val player = PlayerType.HUMAN
        val heapSize = 13
        val gamePhase = GamePhase.IN_PROGRESS
        val intelligenceMode = true

        val gameStateDto = NimGameStateDto(
            maxMoveAllowed = maxMoveAllowed,
            player = player,
            heapSize = heapSize,
            gamePhase = gamePhase,
            intelligenceMode = intelligenceMode
        )

        val result = gameStateDto.toEntity()

        Assertions.assertNotNull(result.id)
        Assertions.assertEquals(maxMoveAllowed, result.maxMoveAllowed)
        Assertions.assertEquals(player, result.player)
        Assertions.assertEquals(heapSize, result.heapSize)
        Assertions.assertEquals(gamePhase, result.gamePhase)
        Assertions.assertEquals(intelligenceMode, result.intelligenceMode)
    }
}