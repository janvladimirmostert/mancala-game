package com.mancala.console.move

import com.mancala.domain.game.Game
import com.mancala.domain.move.Move
import com.mancala.robot.Bot
import com.mancala.robot.RandomBot
import java.lang.IllegalStateException

class RandomBotMove : Move {

	override fun next(game: Game): UInt {
		return when (val result = RandomBot().move(game)) {
			is Bot.MoveResult.NoLegalMoves ->
				throw IllegalStateException("This state should not occur in a valid game")
			is Bot.MoveResult.Success -> result.move
		}
	}

}