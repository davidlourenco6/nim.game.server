package com.holisticon.nim.game.server.unit.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.holisticon.nim.game.server.controller.NimGameController
import com.holisticon.nim.game.server.domain.GamePhase
import com.holisticon.nim.game.server.domain.NimGameState
import com.holisticon.nim.game.server.domain.PlayerType
import com.holisticon.nim.game.server.exception.ExceptionHandlerAdvice
import com.holisticon.nim.game.server.service.NimGameServiceImpl
import io.mockk.clearAllMocks
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@WebMvcTest(controllers = [NimGameController::class, ExceptionHandlerAdvice::class])
class NimGameControllerTest {


    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var nimGameService: NimGameServiceImpl

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    private val gameState = NimGameState(UUID.randomUUID(), 3, PlayerType.HUMAN, 13 , GamePhase.IN_PROGRESS, true)

    //----------------------------------------------- /nim-game/v1/start------------------------------------------------------//

    @Test
    fun `givenValidGameState_whenStartGame_thenOk`() {

        val uuid = UUID.fromString("36d1ba1f-e04f-401f-899b-f2d1c8cb43e5")
        val gameState = NimGameState(uuid, 3, PlayerType.HUMAN, 13 , GamePhase.IN_PROGRESS, true)

        mockStatic(UUID::class.java)
        `when`(UUID.randomUUID()).thenReturn(uuid)

        `when`(nimGameService.startGame(gameState)).thenReturn(
            gameState
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/nim-game/v1/start")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameState))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(gameState.id.toString()))
            .andExpect(jsonPath("$.maxMoveAllowed").value(gameState.maxMoveAllowed))
            .andExpect(jsonPath("$.player").value(gameState.player.toString()))
            .andExpect(jsonPath("$.heapSize").value(gameState.heapSize))
            .andExpect(jsonPath("$.gamePhase").value(gameState.gamePhase.toString()))
            .andExpect(jsonPath("$.intelligenceMode").value(gameState.intelligenceMode))
    }

    //----------------------------------------------- /nim-game/v1/restart ------------------------------------------------------//

    @Test
    fun `givenValidGameState_whenRetartGame_thenOk`() {

        `when`(nimGameService.restartGame(gameState)).thenReturn(
            gameState
        )

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/nim-game/v1/restart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameState))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(gameState.id.toString()))
            .andExpect(jsonPath("$.maxMoveAllowed").value(gameState.maxMoveAllowed))
            .andExpect(jsonPath("$.player").value(gameState.player.toString()))
            .andExpect(jsonPath("$.heapSize").value(gameState.heapSize))
            .andExpect(jsonPath("$.gamePhase").value(gameState.gamePhase.toString()))
            .andExpect(jsonPath("$.intelligenceMode").value(gameState.intelligenceMode))
    }

    //----------------------------------------------- /nim-game/v1/play/$gameId/$move ------------------------------------------------------//

    @Test
    fun `givenValidGameIdAndMove_whenPlayGame_thenOk`() {
        val gameId = UUID.randomUUID()
        val move = 2

        `when`(nimGameService.playerMove(gameId.toString(), move)).thenReturn(gameState)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/nim-game/v1/play/$gameId/$move")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(gameState.id.toString()))
            .andExpect(jsonPath("$.maxMoveAllowed").value(gameState.maxMoveAllowed))
            .andExpect(jsonPath("$.player").value(gameState.player.toString()))
            .andExpect(jsonPath("$.heapSize").value(gameState.heapSize))
            .andExpect(jsonPath("$.gamePhase").value(gameState.gamePhase.toString()))
            .andExpect(jsonPath("$.intelligenceMode").value(gameState.intelligenceMode))
    }

    //----------------------------------------------- /nim-game/v1/fetch/$gameId ------------------------------------------------------//

    @Test
    fun `givenValidGameId_whenFetchGame_thenOk`() {
        val gameId = UUID.randomUUID()

        `when`(nimGameService.fetchGameStatus(gameId.toString())).thenReturn(gameState)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/nim-game/v1/fetch/$gameId")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(gameState.id.toString()))
            .andExpect(jsonPath("$.maxMoveAllowed").value(gameState.maxMoveAllowed))
            .andExpect(jsonPath("$.player").value(gameState.player.toString()))
            .andExpect(jsonPath("$.heapSize").value(gameState.heapSize))
            .andExpect(jsonPath("$.gamePhase").value(gameState.gamePhase.toString()))
            .andExpect(jsonPath("$.intelligenceMode").value(gameState.intelligenceMode))
    }

}