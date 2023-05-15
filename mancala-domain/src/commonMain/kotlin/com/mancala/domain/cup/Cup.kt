package com.mancala.domain.cup

interface Cup {

	val stones: UInt

	sealed interface CupAddResult {
		object TooManyStonesError : CupAddResult
		class Success(val cup: Cup) : CupAddResult
	}

	fun add(stones: UInt): CupAddResult

	sealed interface CupRemResult {
		object NotEnoughStonesError : CupRemResult
		class Success(val cup: Cup) : CupRemResult
	}

	fun rem(stones: UInt): CupRemResult

	interface PlayerCup : Cup
	interface ScoringCup : Cup

	override fun toString(): String

}