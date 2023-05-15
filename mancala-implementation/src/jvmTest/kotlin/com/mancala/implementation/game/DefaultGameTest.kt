package com.mancala.implementation.game

import com.mancala.domain.cup.Cup
import com.mancala.domain.game.Game
import com.mancala.domain.game.GameState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DefaultGameTest {

	@Test
	fun `test that a game can be created correctly`() {

		GameDelegate(
			cups = 12u,
			stonesPerCup = 4u
		).also { game ->

			// test that player 1 is setup correctly
			game.player1.playerCups.also { cups ->
				assertEquals(12, cups.size)
			}.onEach { cup ->
				assertEquals(4u, cup.stones)
			}
			assertEquals(0u, game.player1.scoringCup.stones)

			// test that player 2 is setup correctly
			game.player2.playerCups.also { cups ->
				assertEquals(12, cups.size)
			}.onEach { cup ->
				assertEquals(4u, cup.stones)
			}
			assertEquals(0u, game.player2.scoringCup.stones)
		}

	}

	@Test
	fun `test that the opponent player gets flushed when a player emptied their board and drawing game`() {

		// test that player1's move draws the game
		GameDelegate(
			player1Cups = listOf(0u, 0u, 1u),
			player1ScoringCup = 3u,

			player2Cups = listOf(2u, 1u, 0u),
			player2ScoringCup = 1u,

			state = GameState.Active.PlayerOneToMove
		).also(::println).also {
			println("legal moves: ${it.legalMoves}")
		}.move(2u).also {
			assertIs<Game.MoveResult.GameOver>(it)
			println(it.game)
			assertEquals(listOf(0u, 0u, 0u), it.game.player1.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(4u, it.game.player1.scoringCup.stones)
			assertEquals(listOf(0u, 0u, 0u), it.game.player2.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(4u, it.game.player2.scoringCup.stones)
			assertIs<GameState.Over.Draw>(it.game.state)
		}

		// test that player2's move draws the game
		GameDelegate(
			player2Cups = listOf(0u, 0u, 1u),
			player2ScoringCup = 3u,

			player1Cups = listOf(2u, 1u, 0u),
			player1ScoringCup = 1u,

			state = GameState.Active.PlayerTwoToMove
		).also(::println).also {
			println("legal moves: ${it.legalMoves}")
		}.move(2u).also {
			assertIs<Game.MoveResult.GameOver>(it)
			println(it.game)
			assertEquals(listOf(0u, 0u, 0u), it.game.player1.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(4u, it.game.player1.scoringCup.stones)
			assertEquals(listOf(0u, 0u, 0u), it.game.player2.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(4u, it.game.player2.scoringCup.stones)
			assertIs<GameState.Over.Draw>(it.game.state)
		}

	}

	@Test
	fun `test that the opponent player gets flushed when a player emptied their board and losing game`() {

		// test that player1's move wins the game
		GameDelegate(
			player1Cups = listOf(0u, 0u, 1u),
			player1ScoringCup = 3u,

			player2Cups = listOf(1u, 1u, 0u),
			player2ScoringCup = 1u,

			state = GameState.Active.PlayerOneToMove
		).also(::println).also {
			println(it.legalMoves)
		}.move(2u).also {
			assertIs<Game.MoveResult.GameOver>(it)
			println(it.game)
			assertEquals(listOf(0u, 0u, 0u), it.game.player1.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(4u, it.game.player1.scoringCup.stones)
			assertEquals(listOf(0u, 0u, 0u), it.game.player2.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(3u, it.game.player2.scoringCup.stones)
			assertIs<GameState.Over.PlayerOneWon>(it.game.state)
		}

		// test that player2's move wins the game
		GameDelegate(
			player2Cups = listOf(0u, 0u, 1u),
			player2ScoringCup = 3u,

			player1Cups = listOf(1u, 1u, 0u),
			player1ScoringCup = 1u,

			state = GameState.Active.PlayerTwoToMove
		).also(::println).also {
			println(it.legalMoves)
		}.move(2u).also {
			assertIs<Game.MoveResult.GameOver>(it)
			println(it.game)
			assertEquals(listOf(0u, 0u, 0u), it.game.player2.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(4u, it.game.player2.scoringCup.stones)
			assertEquals(listOf(0u, 0u, 0u), it.game.player1.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(3u, it.game.player1.scoringCup.stones)
			assertIs<GameState.Over.PlayerTwoWon>(it.game.state)
		}
	}

	@Test
	fun `test that the opponent player gets flushed when a player emptied their board and winning game`() {

		// test that player1s' move loses the game
		GameDelegate(
			player1Cups = listOf(0u, 0u, 1u),
			player1ScoringCup = 3u,

			player2Cups = listOf(1u, 3u, 0u),
			player2ScoringCup = 1u,

			state = GameState.Active.PlayerOneToMove
		).also(::println).also {
			println(it.legalMoves)
		}.move(2u).also {
			assertIs<Game.MoveResult.GameOver>(it)
			println(it.game)
			assertEquals(listOf(0u, 0u, 0u), it.game.player1.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(4u, it.game.player1.scoringCup.stones)
			assertEquals(listOf(0u, 0u, 0u), it.game.player2.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(5u, it.game.player2.scoringCup.stones)
			assertIs<GameState.Over.PlayerTwoWon>(it.game.state)
		}

		// test that player2's move loses the game
		GameDelegate(
			player2Cups = listOf(0u, 0u, 1u),
			player2ScoringCup = 3u,

			player1Cups = listOf(1u, 3u, 0u),
			player1ScoringCup = 1u,

			state = GameState.Active.PlayerTwoToMove
		).also(::println).also {
			println(it.legalMoves)
		}.move(2u).also {
			assertIs<Game.MoveResult.GameOver>(it)
			println(it.game)
			assertEquals(listOf(0u, 0u, 0u), it.game.player2.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(4u, it.game.player2.scoringCup.stones)
			assertEquals(listOf(0u, 0u, 0u), it.game.player1.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(5u, it.game.player1.scoringCup.stones)
			assertIs<GameState.Over.PlayerOneWon>(it.game.state)
		}
	}

	@Test
	fun `test that opponent pieces can be captured when landing on an empty cup`() {

		// test that player 1 can capture player 2's pieces
		GameDelegate(
			player1Cups = listOf(1u, 1u, 0u, 1u, 2u, 3u),
			player1ScoringCup = 3u,

			player2Cups = listOf(0u, 0u, 0u, 1u, 0u, 1u),
			player2ScoringCup = 1u,

			state = GameState.Active.PlayerOneToMove
		).also(::println).also {
			println(it.legalMoves)
		}.move(1u).also {
			assertIs<Game.MoveResult.Success>(it)
			println(it.game)
			assertEquals(listOf(1u, 0u, 0u, 1u, 2u, 3u), it.game.player1.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(5u, it.game.player1.scoringCup.stones)
			assertEquals(listOf(0u, 0u, 0u, 0u, 0u, 1u), it.game.player2.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(1u, it.game.player2.scoringCup.stones)
		}

		// test that player 2 can capture player 1's pieces
		GameDelegate(
			player1Cups = listOf(1u, 1u, 0u, 1u, 2u, 3u),
			player1ScoringCup = 3u,

			player2Cups = listOf(0u, 0u, 0u, 1u, 0u, 1u),
			player2ScoringCup = 1u,

			state = GameState.Active.PlayerTwoToMove
		).also(::println).also {
			println(it.legalMoves)
		}.move(3u).also {
			assertIs<Game.MoveResult.Success>(it)
			println(it.game)
			assertEquals(listOf(1u, 0u, 0u, 1u, 2u, 3u), it.game.player1.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(3u, it.game.player1.scoringCup.stones)
			assertEquals(listOf(0u, 0u, 0u, 0u, 0u, 1u), it.game.player2.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(3u, it.game.player2.scoringCup.stones)
		}

	}

	@Test
	fun `test that capture doesn't apply when the opponent cup is empty`() {

		// test that player 1 doesn't capture on player 2's empty cup
		GameDelegate(
			player1Cups = listOf(1u, 0u, 0u, 1u, 2u, 3u),
			player1ScoringCup = 3u,

			player2Cups = listOf(1u, 0u, 0u, 1u, 0u, 1u),
			player2ScoringCup = 1u,

			state = GameState.Active.PlayerOneToMove
		).also(::println).also {
			println(it.legalMoves)
		}.move(0u).also {
			assertIs<Game.MoveResult.Success>(it)
			println(it.game)
			assertEquals(listOf(0u, 1u, 0u, 1u, 2u, 3u), it.game.player1.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(3u, it.game.player1.scoringCup.stones)
			assertEquals(listOf(1u, 0u, 0u, 1u, 0u, 1u), it.game.player2.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(1u, it.game.player2.scoringCup.stones)
		}

		// test that player 2 doesn't capture on player 1's empty cup
		GameDelegate(
			player1Cups = listOf(1u, 0u, 0u, 1u, 0u, 3u),
			player1ScoringCup = 3u,

			player2Cups = listOf(1u, 0u, 0u, 1u, 0u, 1u),
			player2ScoringCup = 1u,

			state = GameState.Active.PlayerTwoToMove
		).also(::println).also {
			println(it.legalMoves)
		}.move(0u).also {
			assertIs<Game.MoveResult.Success>(it)
			println(it.game)
			assertEquals(listOf(1u, 0u, 0u, 1u, 0u, 3u), it.game.player1.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(3u, it.game.player1.scoringCup.stones)
			assertEquals(listOf(0u, 1u, 0u, 1u, 0u, 1u), it.game.player2.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(1u, it.game.player2.scoringCup.stones)
		}

	}

	@Test
	fun `test that capturing still works when wrapping around`() {
		GameDelegate(
			player1Cups = listOf(1u, 1u, 0u, 1u, 2u, 10u),
			player1ScoringCup = 3u,

			player2Cups = listOf(1u, 0u, 0u, 1u, 0u, 1u),
			player2ScoringCup = 1u,

			state = GameState.Active.PlayerOneToMove
		).also(::println).also {
			println(it.legalMoves)
		}.move(5u).also {
			assertIs<Game.MoveResult.Success>(it)
			println(it.game)
			assertEquals(listOf(2u, 2u, 0u, 1u, 2u, 0u), it.game.player1.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(7u, it.game.player1.scoringCup.stones)
			assertEquals(listOf(2u, 1u, 1u, 0u, 1u, 2u), it.game.player2.playerCups.map(Cup.PlayerCup::stones))
			assertEquals(1u, it.game.player2.scoringCup.stones)
		}
	}

}