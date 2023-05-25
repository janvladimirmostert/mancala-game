package com.mancala.domain.game

import com.mancala.domain.cup.Cup
import com.mancala.domain.player.Player

interface GameRules {

	sealed interface StoneMovementResult {
		object IllegalMoveError : StoneMovementResult
		object NoStonesToMoveError : StoneMovementResult
		data class Success(
			val playerCups: List<Cup>,
			val playerScoringCup: Cup,
			val opponentCups: List<Cup>,
			val opponentScoringCup: Cup,
			val lastMoveOnPlayerEmptyCup: Boolean,
			val lastMoveOnPlayerScoringCup: Boolean,
			val playerGetsAnotherMove: Boolean,
			val gameOver: Boolean,
			val playerWon: Boolean,
			val opponentWon: Boolean,
			val gameDrew: Boolean,
		) : StoneMovementResult
	}

	fun calculateStoneMovement(
		cup: UInt,
		player: Player,
		opponent: Player
	): StoneMovementResult

}