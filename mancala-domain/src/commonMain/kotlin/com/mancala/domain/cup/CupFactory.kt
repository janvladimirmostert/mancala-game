package com.mancala.domain.cup

interface CupFactory {

	fun createPlayerCup(stones: UInt): Cup.PlayerCup
	fun createScoringCup(stones: UInt): Cup.ScoringCup

}