package com.mancala.robot

import com.mancala.domain.game.Game

class RandomBot : Bot {

	override fun move(game: Game): Bot.MoveResult {
		return game.legalMoves.let {
			if (it.isEmpty()) {
				Bot.MoveResult.NoLegalMoves
			} else {
				Bot.MoveResult.Success(it.random())
			}
		}
	}

}