package com.holisticon.nim.game.server.repository

import com.holisticon.nim.game.server.domain.NimGameState
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface NimGameRepository : CrudRepository<NimGameState, String>