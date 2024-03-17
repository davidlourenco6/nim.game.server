package com.holisticon.nim.game.server.domain

import com.holisticon.nim.game.server.controller.dto.NimGameStateDto
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.util.UUID

@RedisHash("NimGameState")
data class NimGameState(
    @Id
    var id: UUID = UUID.randomUUID(),
    var maxMoveAllowed : Int,
    var player: PlayerType,
    var heapSize: Int,
    var gamePhase: GamePhase = GamePhase.IN_PROGRESS,
    var intelligenceMode: Boolean
) {

    fun toDto(): NimGameStateDto {
        return NimGameStateDto(
            id = this.id,
            maxMoveAllowed = this.maxMoveAllowed,
            player = this.player,
            heapSize = this.heapSize,
            gamePhase = this.gamePhase,
            intelligenceMode = this.intelligenceMode
        )
    }

}