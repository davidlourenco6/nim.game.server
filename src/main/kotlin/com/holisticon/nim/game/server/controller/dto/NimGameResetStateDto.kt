package com.holisticon.nim.game.server.controller.dto

import com.holisticon.nim.game.server.domain.GamePhase
import com.holisticon.nim.game.server.domain.NimGameState
import com.holisticon.nim.game.server.domain.PlayerType
import java.util.UUID

class NimGameResetStateDto(
    private var id: UUID,
    private var maxMoveAllowed : Int,
    private var player: PlayerType,
    private var heapSize: Int,
    private var gamePhase: GamePhase,
    private var intelligenceMode: Boolean
) {

    fun toEntity(): NimGameState {
        return NimGameState(
            id = this.id,
            maxMoveAllowed = this.maxMoveAllowed,
            player = this.player,
            heapSize = this.heapSize,
            gamePhase = this.gamePhase,
            intelligenceMode = this.intelligenceMode
        )
    }

}