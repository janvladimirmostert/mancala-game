package com.mancala.implementation.cup

import com.mancala.domain.cup.Cup
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CupDelegateTest {

	@Test
	fun `test that we can change the behavior of a cup delegate`() {

		// This is a mutable ScoringCup capped at 10 stones
		// Mutable ScoringCup might be slightly faster than
		// the DefaultScoringCup which is immutable at the
		// cost of having to deal with synchronisation when
		// dealing with concurrency.
		val mutableCupCappedAt10 = object : Cup.ScoringCup {

			private var _stones = 0u
			override val stones: UInt
				get() = _stones

			override fun add(stones: UInt): Cup.CupAddResult {
				return if (_stones + stones > 10u) {
					Cup.CupAddResult.TooManyStonesError
				} else {
					_stones += stones
					Cup.CupAddResult.Success(this)
				}
			}

			override fun rem(stones: UInt): Cup.CupRemResult {
				return if (_stones.toInt() - stones.toInt() < 0) {
					Cup.CupRemResult.NotEnoughStonesError
				} else {
					_stones -= stones
					Cup.CupRemResult.Success(this)
				}
			}

			override fun toString(): String {
				return this.stones.toString()
			}

		}

		// create a new cup delegate but replace the internals with a mutable cup
		val cupDelegate = ScoringCupDelegate(mutableCupCappedAt10)

		// test that adding is now mutable and capped at 10 stones max

		cupDelegate.add(3u).also {
			assertIs<Cup.CupAddResult.Success>(it)
			assertEquals(3u, it.cup.stones)
		}

		cupDelegate.add(7u).also {
			assertIs<Cup.CupAddResult.Success>(it)
			assertEquals(10u, it.cup.stones)
		}

		cupDelegate.add(1u).also {
			assertIs<Cup.CupAddResult.TooManyStonesError>(it)
			assertEquals(10u, mutableCupCappedAt10.stones)
		}

		// test that removing is now mutable and capped at 0 stones min

		cupDelegate.rem(6u).also {
			assertIs<Cup.CupRemResult.Success>(it)
			assertEquals(4u, it.cup.stones)
		}

		cupDelegate.rem(4u).also {
			assertIs<Cup.CupRemResult.Success>(it)
			assertEquals(0u, it.cup.stones)
		}

		cupDelegate.rem(1u).also {
			assertIs<Cup.CupRemResult.NotEnoughStonesError>(it)
			assertEquals(0u, mutableCupCappedAt10.stones)
		}
	}

}