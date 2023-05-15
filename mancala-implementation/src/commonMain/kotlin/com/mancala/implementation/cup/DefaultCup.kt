package com.mancala.implementation.cup

import com.mancala.domain.cup.Cup
import com.mancala.domain.cup.CupFactory
import com.mancala.implementation.cup.CupStringify.stringify

@Suppress("DataClassPrivateConstructor")
data class DefaultCup private constructor(
	override val stones: UInt = 0u
) : Cup.PlayerCup, Cup.ScoringCup {

	override fun add(stones: UInt): Cup.CupAddResult {
		return Cup.CupAddResult.Success(
			DefaultCup(stones = this.stones + stones)
		)
	}

	override fun rem(stones: UInt): Cup.CupRemResult {
		return if (stones > this.stones) {
			Cup.CupRemResult.NotEnoughStonesError
		} else {
			Cup.CupRemResult.Success(
				DefaultCup(stones = this.stones - stones)
			)
		}
	}

	override fun toString(): String {
		return this.stringify(cupPadding = 0u)
	}

	companion object : CupFactory {

		override fun createPlayerCup(stones: UInt): Cup.PlayerCup {
			return DefaultCup(stones = stones)
		}

		override fun createScoringCup(stones: UInt): Cup.ScoringCup {
			return DefaultCup(stones = stones)
		}

	}

}