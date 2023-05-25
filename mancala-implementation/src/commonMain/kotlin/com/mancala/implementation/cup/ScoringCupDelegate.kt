package com.mancala.implementation.cup

import com.mancala.domain.cup.Cup
import com.mancala.domain.cup.CupFactory

class ScoringCupDelegate(
	private val scoringCup: Cup.ScoringCup
) : Cup.ScoringCup by scoringCup {

	constructor(
		stones: UInt,
		cupFactory: CupFactory = DefaultCup
	) : this(
		scoringCup = cupFactory.createScoringCup(stones = stones)
	)

}