package com.mancala.domain.move

import com.mancala.domain.game.Game

interface Move {

	fun next(game: Game): UInt

}