package com.mancala.console

import com.mancala.console.move.ConsoleInputMove
import com.mancala.console.move.RandomBotMove
import com.mancala.domain.game.Game
import com.mancala.domain.game.GameState
import com.mancala.domain.move.Move
import com.mancala.implementation.game.GameDelegate
import com.mancala.util.console.COLOUR
import com.mancala.util.console.Read
import com.mancala.util.console.toColour
import java.lang.IllegalStateException

object Play {

	@JvmStatic
	fun main(args: Array<String>) {

		javaClass.getResourceAsStream("/ascii.txt")?.use {
			String(it.readAllBytes()).toColour(COLOUR.ANSI_GREEN)
		}.also(::println)

		// capture player names
		println("new game starting ...")
		println("enter name 'random' to play against the random bot")
		println()
		println("enter name for Player1:")
		val player1Name = Read.readlnUntilNotEmpty()
		println("enter name for Player2:")
		val player2Name = Read.readlnUntilNotEmpty()

		// create move factories
		val player1Mover: Move = when (player1Name) {
			"random" -> RandomBotMove()
			else -> ConsoleInputMove()
		}
		val player2Mover: Move = when (player2Name) {
			"random" -> RandomBotMove()
			else -> ConsoleInputMove()
		}

		// create a new game
		var game: Game = GameDelegate(
			player1Name = player1Name,
			player2Name = player2Name,
			cups = 6u,
			stonesPerCup = 6u
		)

		val printGame: (Game) -> Unit = {
			println(it)
		}

		// read move input while game is not over
		do {
			printGame(game)

			game.playerTurn?.also { player ->
				println("${player.name}, it is your turn!")
				println("legal moves are: ${game.legalMoves}")
			}

			val move = if (game.player1Turn) {
				player1Mover.next(game)
			} else if (game.player2Turn) {
				player2Mover.next(game)
			} else {
				throw IllegalStateException("Game can't be active while it's neither player's move")
			}
			println("${game.playerTurn?.name} moved $move")

			when (val result = game.move(move)) {
				is Game.MoveResult.Success -> game = result.game
				is Game.MoveResult.GameOver -> break
				is Game.MoveResult.IllegalMoveError -> {
					println("that move is not allowed, ${game.playerTurn?.name}")
				}

				is Game.MoveResult.NoStonesToMoveError -> {
					println("that cup has no stones to move, ${game.playerTurn?.name}")
				}
			}
		} while (game.isActive)

		// output game result
		if (game.isOver) {
			when (game.state as GameState.Over) {
				GameState.Over.Draw -> {
					printGame(game)
					println("game ended in a draw!")
				}

				GameState.Over.PlayerOneWon -> {
					printGame(game)
					println("${game.player1.name} won this one!")
				}

				GameState.Over.PlayerTwoWon -> {
					printGame(game)
					println("${game.player2.name} won this one!")
				}
			}
		}

	}

}