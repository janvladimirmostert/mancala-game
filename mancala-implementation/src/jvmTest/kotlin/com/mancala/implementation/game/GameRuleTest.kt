package com.mancala.implementation.game

import com.mancala.domain.cup.Cup
import com.mancala.domain.game.GameRules
import com.mancala.domain.player.Player
import com.mancala.implementation.player.PlayerDelegate
import org.junit.jupiter.api.assertThrows
import kotlin.test.*

class GameRuleTest {

	private val defaultGameRules = GameRulesDelegate()

	private fun player(vararg cups: Int): Player {
		return PlayerDelegate(
			cups = cups.slice(0 until cups.lastIndex).map {
				it.toUInt()
			},
			score = cups.last().toUInt()
		)
	}

	@Test
	fun `test that spreading empty cup returns not enough stones error`() {

		defaultGameRules.calculateStoneMovement(
			cup = 0u,
			player = player(0, 0),
			opponent = player(0, 0),
		).also {
			assertIs<GameRules.StoneMovementResult.NoStonesToMoveError>(it)
		}

	}

	@Test
	fun `test that spreading invalid cup returns illegal move error`() {

		defaultGameRules.calculateStoneMovement(
			cup = 1u,
			player = player(0, 0),
			opponent = player(0, 0)
		).also {
			assertIs<GameRules.StoneMovementResult.IllegalMoveError>(it)
		}

	}

	@Test
	fun `test that we can spread stones from any cup`() {

		val player1 = player(1, 1, 1, 0)
		val player2 = player(1, 1, 1, 0)

		defaultGameRules.calculateStoneMovement(
			cup = 0u,
			player = player1,
			opponent = player2
		).also {
			assertIs<GameRules.StoneMovementResult.Success>(it)
			assertEquals(
				listOf(0u, 2u, 1u),
				it.playerCups.map(Cup::stones)
			)
			assertEquals(0u, it.playerScoringCup.stones)
			assertFalse(it.lastMoveOnPlayerEmptyCup)
			assertFalse(it.lastMoveOnPlayerScoringCup)
			assertFalse(it.playerGetsAnotherMove)
		}

		defaultGameRules.calculateStoneMovement(
			cup = 1u,
			player = player1,
			opponent = player2
		).also {
			assertIs<GameRules.StoneMovementResult.Success>(it)
			assertEquals(
				listOf(1u, 0u, 2u),
				it.playerCups.map(Cup::stones)
			)
			assertEquals(0u, it.playerScoringCup.stones)
			assertFalse(it.lastMoveOnPlayerEmptyCup)
			assertFalse(it.lastMoveOnPlayerScoringCup)
			assertFalse(it.playerGetsAnotherMove)
		}

		defaultGameRules.calculateStoneMovement(
			cup = 2u,
			player = player1,
			opponent = player2
		).also {
			assertIs<GameRules.StoneMovementResult.Success>(it)
			assertEquals(
				listOf(1u, 1u, 0u),
				it.playerCups.map(Cup::stones)
			)
			assertEquals(1u, it.playerScoringCup.stones)
			assertFalse(it.lastMoveOnPlayerEmptyCup)
			assertTrue(it.lastMoveOnPlayerScoringCup)
			assertTrue(it.playerGetsAnotherMove)
		}

	}

	@Test
	fun `test that we can wrap around to the opponent pieces and back when spreading`() {

		val player1 = player(5, 3, 5, 0)
		val player2 = player(1, 1, 1, 0)

		defaultGameRules.calculateStoneMovement(
			cup = 0u,
			player = player1,
			opponent = player2
		).also {
			assertIs<GameRules.StoneMovementResult.Success>(it)
			assertEquals(
				listOf(0u, 4u, 6u),
				it.playerCups.map(Cup::stones)
			)
			assertEquals(1u, it.playerScoringCup.stones)
			assertEquals(
				listOf(2u, 2u, 1u),
				it.opponentCups.map(Cup::stones)
			)
			assertEquals(0u, it.opponentScoringCup.stones)
		}

		defaultGameRules.calculateStoneMovement(
			cup = 1u,
			player = player1,
			opponent = player2
		).also {
			assertIs<GameRules.StoneMovementResult.Success>(it)
			assertEquals(
				listOf(5u, 0u, 6u),
				it.playerCups.map(Cup::stones)
			)
			assertEquals(1u, it.playerScoringCup.stones)
			assertEquals(
				listOf(2u, 1u, 1u),
				it.opponentCups.map(Cup::stones)
			)
			assertEquals(0u, it.opponentScoringCup.stones)
		}

		defaultGameRules.calculateStoneMovement(
			cup = 2u,
			player = player1,
			opponent = player2
		).also {
			assertIs<GameRules.StoneMovementResult.Success>(it)
			assertEquals(
				listOf(6u, 3u, 0u),
				it.playerCups.map(Cup::stones)
			)
			assertEquals(1u, it.playerScoringCup.stones)
			assertEquals(
				listOf(2u, 2u, 2u),
				it.opponentCups.map(Cup::stones)
			)
			assertEquals(0u, it.opponentScoringCup.stones)
		}

	}

	@Test
	fun `test that we can spread around multiple times`() {

		val player1 = player(5, 300, 5, 0)
		val player2 = player(1, 1, 1, 0)

		defaultGameRules.calculateStoneMovement(
			cup = 1u,
			player = player1,
			opponent = player2
		).also {
			assertIs<GameRules.StoneMovementResult.Success>(it)
			assertEquals(
				listOf(48u, 42u, 48u),
				it.playerCups.map(Cup::stones)
			)
			assertEquals(43u, it.playerScoringCup.stones)
			assertEquals(
				listOf(44u, 44u, 44u),
				it.opponentCups.map(Cup::stones)
			)
			assertEquals(0u, it.opponentScoringCup.stones)
		}

	}

	@Test
	fun `test that we get a second move when landing on the scoring cup`() {

		val player1 = player(3, 1, 1, 0)
		val player2 = player(3, 1, 1, 0)

		defaultGameRules.calculateStoneMovement(
			cup = 0u,
			player = player1,
			opponent = player2
		).also {
			assertIs<GameRules.StoneMovementResult.Success>(it)
			assertEquals(
				listOf(0u, 2u, 2u),
				it.playerCups.map(Cup::stones)
			)
			assertEquals(1u, it.playerScoringCup.stones)
			assertFalse(it.lastMoveOnPlayerEmptyCup)
			assertTrue(it.lastMoveOnPlayerScoringCup)
			assertTrue(it.playerGetsAnotherMove)
		}

	}

	@Test
	fun `test that we can capture opponent pieces landing an an empty cup`() {

		val player1 = player(2, 1, 0, 0)
		val player2 = player(120, 1, 2, 1)

		defaultGameRules.calculateStoneMovement(
			cup = 0u,
			player = player1,
			opponent = player2
		).also {
			assertIs<GameRules.StoneMovementResult.Success>(it)
			assertEquals(
				listOf(0u, 2u, 0u),
				it.playerCups.map(Cup::stones)
			)
			assertEquals(121u, it.playerScoringCup.stones)
			assertEquals(
				listOf(0u, 1u, 2u),
				it.opponentCups.map(Cup::stones)
			)
			assertEquals(1u, it.opponentScoringCup.stones)
		}

	}

	@Test
	fun `test that capture doesn't kick in on an empty opponent cup`() {

		val player1 = player(2, 1, 0, 0)
		val player2 = player(0, 1, 2, 1)

		defaultGameRules.calculateStoneMovement(
			cup = 0u,
			player = player1,
			opponent = player2
		).also {
			assertIs<GameRules.StoneMovementResult.Success>(it)
			assertEquals(
				listOf(0u, 2u, 1u),
				it.playerCups.map(Cup::stones)
			)
			assertEquals(0u, it.playerScoringCup.stones)
			assertEquals(
				listOf(0u, 1u, 2u),
				it.opponentCups.map(Cup::stones)
			)
			assertEquals(1u, it.opponentScoringCup.stones)
		}

	}

	@Test
	fun `test that running out of stones on either sides flushes all the player cups to the scoring cups`() {

		val player1 = player(0, 0, 1, 0)
		val player2 = player(2, 1, 120, 1)

		defaultGameRules.calculateStoneMovement(
			cup = 2u,
			player = player1,
			opponent = player2
		).also {
			assertIs<GameRules.StoneMovementResult.Success>(it)
			assertTrue(it.gameOver)
			assertEquals(
				listOf(0u, 0u, 0u),
				it.playerCups.map(Cup::stones)
			)
			assertEquals(1u, it.playerScoringCup.stones)
			assertEquals(
				listOf(0u, 0u, 0u),
				it.opponentCups.map(Cup::stones)
			)
			assertEquals(124u, it.opponentScoringCup.stones)
		}

	}

	@Test
	fun `test that calculating stone movement on players with different numbers of cups fails`() {

		val player1 = player(0, 0)
		val player2 = player(2, 1, 0)

		assertThrows<IllegalStateException> {
			defaultGameRules.calculateStoneMovement(
				cup = 2u,
				player = player1,
				opponent = player2
			)
		}

	}

	@Test
	fun `test that landing on an empty slot where the player filled the opponent cup gets flushed`() {
		val player1 = player(0, 0, 0, 0, 10, 4, 37)
		val player2 = player(7, 2, 1, 1, 0, 0, 10)
		defaultGameRules.calculateStoneMovement(
			cup = 4u,
			player = player1,
			opponent = player2
		).also {
			assertIs<GameRules.StoneMovementResult.Success>(it)
			assertEquals(
				listOf(1u, 0u, 0u, 0u, 0u, 5u),
				it.playerCups.map(Cup::stones)
			)
			assertEquals(40u, it.playerScoringCup.stones)
			assertEquals(
				listOf(8u, 3u, 2u, 2u, 0u, 1u),
				it.opponentCups.map(Cup::stones)
			)
			assertEquals(10u, it.opponentScoringCup.stones)
			assertTrue(it.lastMoveOnPlayerEmptyCup)
			assertFalse(it.lastMoveOnPlayerScoringCup)
		}

	}

}