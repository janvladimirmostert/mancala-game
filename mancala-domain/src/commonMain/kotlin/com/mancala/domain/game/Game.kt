package com.mancala.domain.game

import com.mancala.domain.player.Player

interface Game {

	val player1: Player
	val player2: Player
	val state: GameState
	val rules: GameRules

	val legalMoves: List<UInt>
	val player1Turn: Boolean
		get() = state is GameState.Active.PlayerOneToMove
	val player1Won: Boolean
		get() = state is GameState.Over.PlayerOneWon
	val player2Turn: Boolean
		get() = state is GameState.Active.PlayerTwoToMove
	val player2Won: Boolean
		get() = state is GameState.Over.PlayerTwoWon
	val nobodyWon: Boolean
		get() = state is GameState.Over.Draw
	val playerTurn: Player?

	val isActive: Boolean
		get() = state is GameState.Active

	val isOver: Boolean
		get() = state is GameState.Over

	sealed interface MoveResult {
		object IllegalMoveError : MoveResult
		object NoStonesToMoveError : MoveResult
		data class GameOver(
			val game: Game
		) : MoveResult
		data class Success(
			val game: Game
		) : MoveResult
	}

	fun move(cup: UInt): MoveResult

	override fun toString(): String

}