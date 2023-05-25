package com.mancala.console.move

import com.mancala.domain.game.Game
import com.mancala.domain.move.Move
import com.mancala.util.console.Read

class ConsoleInputMove : Move {

	override fun next(game: Game): UInt {
		return Read.readUIntOrNull()
	}

}