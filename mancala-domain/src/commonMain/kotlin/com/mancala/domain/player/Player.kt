package com.mancala.domain.player

import com.mancala.domain.cup.Cup

interface Player {
	val name: String
	val playerCups: List<Cup.PlayerCup>
	val scoringCup: Cup.ScoringCup

	val allCups: List<Cup>
		get() = listOf(*playerCups.toTypedArray(), scoringCup)

	override fun toString(): String

}