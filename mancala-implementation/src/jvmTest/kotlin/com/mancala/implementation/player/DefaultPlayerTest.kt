package com.mancala.implementation.player

import com.mancala.domain.cup.Cup
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultPlayerTest {

	@Test
	fun `test that a player can be created with filled cups`() {

		PlayerDelegate(name = "Player1", cups = 20u, stonesPerCup = 4u).also { player ->
			// check that total number of stones is 20x1
			assertEquals(20u * 4u, player.playerCups.map(Cup::stones).sum())

			// check that total number of stones is 20x1 including the scoring cup
			assertEquals(20u * 4u, player.allCups.map(Cup::stones).sum())

			// check that each player cup has exactly 4 stones
			player.playerCups.onEach { playerCup ->
				assertEquals(4u, playerCup.stones)
			}

			// check that the scoring cup is empty
			assertEquals(0u, player.scoringCup.stones)

		}

	}

}