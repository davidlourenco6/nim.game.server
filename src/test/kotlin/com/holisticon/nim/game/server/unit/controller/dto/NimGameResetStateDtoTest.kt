package com.holisticon.nim.game.server.unit.controller.dto

import com.holisticon.nim.game.server.controller.dto.NimGameResetStateDto
import com.holisticon.nim.game.server.domain.GamePhase
import com.holisticon.nim.game.server.domain.PlayerType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class NimGameResetStateDtoTest {

    //----------------------------------------------- NimGameResetStateDto.toEntity() ------------------------------------------------------//

    @Test
    fun `givenValidNimGameResetStateDto_whenToEntity_thenNimGameResetState`() {
        val id = UUID.randomUUID()
        val maxMoveAllowed = 3
        val player = PlayerType.HUMAN
        val heapSize = 13
        val gamePhase = GamePhase.IN_PROGRESS
        val intelligenceMode = true

        val resetStateDto = NimGameResetStateDto(
            id = id,
            maxMoveAllowed = maxMoveAllowed,
            player = player,
            heapSize = heapSize,
            gamePhase = gamePhase,
            intelligenceMode = intelligenceMode
        )

        val result = resetStateDto.toEntity()

        Assertions.assertEquals(id, result.id)
        Assertions.assertEquals(maxMoveAllowed, result.maxMoveAllowed)
        Assertions.assertEquals(player, result.player)
        Assertions.assertEquals(heapSize, result.heapSize)
        Assertions.assertEquals(gamePhase, result.gamePhase)
        Assertions.assertEquals(intelligenceMode, result.intelligenceMode)
    }

}