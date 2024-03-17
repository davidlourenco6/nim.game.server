package com.holisticon.nim.game.server.controller.dto

import com.holisticon.nim.game.server.domain.GamePhase
import com.holisticon.nim.game.server.domain.NimGameState
import com.holisticon.nim.game.server.domain.PlayerType
import java.util.UUID

class NimGameStateDto(
    var id: UUID? = null,
    var maxMoveAllowed : Int = 3,
    var player: PlayerType = PlayerType.HUMAN,
    var heapSize: Int = 13,
    var gamePhase: GamePhase = GamePhase.IN_PROGRESS,
    var intelligenceMode: Boolean
) {

    fun toEntity(): NimGameState {
        return NimGameState(
            maxMoveAllowed = this.maxMoveAllowed,
            player = this.player,
            heapSize = this.heapSize,
            gamePhase = this.gamePhase,
            intelligenceMode = this.intelligenceMode
        )
    }
}