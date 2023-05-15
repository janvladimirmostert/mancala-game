package com.mancala.implementation.cup

import com.mancala.domain.cup.Cup
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DefaultCupTest {

	@Test
	fun `check that the player cup doesn't mutate when adding stones`() {

		val cup = PlayerCupDelegate(0u)
		assertEquals(0u, cup.stones)

		cup.add(0u).also {
			assertIs<Cup.CupAddResult.Success>(it)
			assertEquals(0u, it.cup.stones)
		}

		cup.add(1u).also {
			assertIs<Cup.CupAddResult.Success>(it)
			assertEquals(1u, it.cup.stones)
		}
	}

	@Test
	fun `check that the player cup doesn't mutate when removing stones`() {

		val cup = PlayerCupDelegate(1u)
		assertEquals(1u, cup.stones)

		cup.rem(0u).also {
			assertIs<Cup.CupRemResult.Success>(it)
			assertEquals(1u, it.cup.stones)
		}

		cup.rem(1u).also {
			assertIs<Cup.CupRemResult.Success>(it)
			assertEquals(0u, it.cup.stones)
		}

		cup.rem(2u).also {
			assertIs<Cup.CupRemResult.NotEnoughStonesError>(it)
		}

	}

	@Test
	fun `check that the scoring cup doesn't mutate when adding stones`() {

		val cup = ScoringCupDelegate(0u)
		assertEquals(0u, cup.stones)

		cup.add(0u).also {
			assertIs<Cup.CupAddResult.Success>(it)
			assertEquals(0u, it.cup.stones)
		}

		cup.add(1u).also {
			assertIs<Cup.CupAddResult.Success>(it)
			assertEquals(1u, it.cup.stones)
		}
	}

	@Test
	fun `check that the scoring cup doesn't mutate when removing stones`() {

		val cup = ScoringCupDelegate(1u)
		assertEquals(1u, cup.stones)

		cup.rem(0u).also {
			assertIs<Cup.CupRemResult.Success>(it)
			assertEquals(1u, it.cup.stones)
		}

		cup.rem(1u).also {
			assertIs<Cup.CupRemResult.Success>(it)
			assertEquals(0u, it.cup.stones)
		}

		cup.rem(2u).also {
			assertIs<Cup.CupRemResult.NotEnoughStonesError>(it)
		}

	}

}