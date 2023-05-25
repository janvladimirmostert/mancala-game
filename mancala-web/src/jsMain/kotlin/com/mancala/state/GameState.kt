package com.mancala.state

import com.mancala.domain.game.Game
import com.mancala.implementation.game.GameDelegate

@JsExport
@ExperimentalJsExport
@Suppress("unused", "MemberVisibilityCanBePrivate")
class GameState : ObservableState() {

	private var game by watching<Game?>(initialValue = null)

	var ready by watching<Boolean>(initialValue = false) {
		if (it) {
			this.game = GameDelegate(
				player1Name = this.player1Name ?: "Player1",
				player2Name = this.player2Name ?: "Player2",
				cups = 6u,
				stonesPerCup = 6u,
			).also(::println)
			if (game?.player1Turn == true) {
				player1Message = "Player1 always starts just like in chess ..."
				player2Message = "Waiting for $player2Name to move"
			} else if (game?.player2Turn == true) {
				player1Message = "Waiting for $player1Name to move"
				player2Message = "Somehow player2 gets to start today ..."
			}
		}
	}
	var player1Name by watching<String?>(initialValue = "Player1")
	var player2Name by watching<String?>(initialValue = "Player2")

	var player1Message by watching<String?>(initialValue = null)
	var player2Message by watching<String?>(initialValue = null)

	val player1Cups: Array<Int>
		get() = game?.player1?.playerCups?.map { it.stones.toInt() }?.toTypedArray() ?: emptyArray()
	val player1ScoringCup: Int
		get() = game?.player1?.scoringCup?.stones?.toInt() ?: 0

	val player2Cups: Array<Int>
		get() = game?.player2?.playerCups?.map { it.stones.toInt() }?.toTypedArray() ?: emptyArray()
	val player2ScoringCup: Int
		get() = game?.player2?.scoringCup?.stones?.toInt() ?: 0

	val player1Turn: Boolean
		get() = game?.player1Turn == true

	val player2Turn: Boolean
		get() = game?.player2Turn == true

	fun move(move: Int) {
		println("legal moves for ${game?.playerTurn?.name} are ${game?.legalMoves}")
		println("${game?.playerTurn?.name} moved $move")
		when (val result = game?.move(move.toUInt())) {
			null -> {
				// ignore
				console.error("game is null")
			}
			is Game.MoveResult.Success -> {
				println(result.game)
				if (game?.player1Turn == true && result.game.player1Turn) {
					player1Message = "Nice move! You get another turn."
					player2Message = "Waiting for $player1Name, they have another move ..."
				} else if (game?.player2Turn == true && result.game.player2Turn) {
					player2Message = "Nice move! You get another turn."
					player1Message = "Waiting for $player2Name, they have another move ..."
				} else {
					if (result.game.player1Turn) {
						player1Message = "It is your turn, $player1Name."
						player2Message = "Waiting for $player1Name ..."
					} else if (result.game.player2Turn) {
						player2Message = "It is your turn, $player2Name."
						player1Message = "Waiting for $player2Name ..."
					}
				}
				this.game = result.game
			}
			is Game.MoveResult.GameOver -> {
				println(result.game)
				if (result.game.player1Won) {
					player1Message = "You won the game!"
					player2Message = "$player1Name won the game."
				} else if (result.game.player2Won) {
					player2Message = "You won the game!"
					player1Message = "$player2Name won the game."
				} else if (result.game.nobodyWon) {
					player1Message = "Game ended in a draw."
					player2Message = "Game ended in a draw."
				}
				this.game = result.game
			}
			is Game.MoveResult.IllegalMoveError -> {
				"That is not a legal move".also {
					if (game?.player1Turn == true) {
						player1Message = it
					} else if (game?.player2Turn == true) {
						player2Message = it
					}
				}
			}
			is Game.MoveResult.NoStonesToMoveError -> {
				"Nothing to move, pick something else".also {
					if (game?.player1Turn == true) {
						player1Message = it
					} else if (game?.player2Turn == true) {
						player2Message = it
					}
				}
			}
		}

	}
}