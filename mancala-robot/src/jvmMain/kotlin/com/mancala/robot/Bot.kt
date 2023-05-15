package com.mancala.robot

import com.mancala.domain.game.Game

interface Bot {

	sealed interface MoveResult {
		object NoLegalMoves : MoveResult
		data class Success(val move: UInt) : MoveResult
	}

	fun move(game: Game): MoveResult

}