package com.mancala.domain.player

import com.mancala.domain.cup.Cup
import com.mancala.domain.cup.CupFactory

interface PlayerFactory {

	fun create(
		name: String,
		playerCups: List<Cup.PlayerCup>,
		scoringCup: Cup.ScoringCup
	): Player

	fun create(
		name: String,
		cups: UInt,
		stonesPerCup: UInt,
		cupFactory: CupFactory
	): Player {
		return create(
			name = name,
			playerCups = (1u..cups).map {
				cupFactory.createPlayerCup(
					stones = stonesPerCup
				)
			},
			scoringCup = cupFactory.createScoringCup(
				stones = 0u
			)
		)
	}

	fun create(
		name: String,
		cups: List<UInt>,
		score: UInt,
		cupFactory: CupFactory
	): Player {
		return create(
			name = name,
			playerCups = cups.map {
				cupFactory.createPlayerCup(stones = it)
			},
			scoringCup = cupFactory.createScoringCup(stones = score)
		)
	}

}