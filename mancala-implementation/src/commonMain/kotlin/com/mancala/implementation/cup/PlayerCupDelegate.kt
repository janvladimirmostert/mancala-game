package com.mancala.implementation.cup

import com.mancala.domain.cup.Cup
import com.mancala.domain.cup.CupFactory

class PlayerCupDelegate(
	private val playerCup: Cup.PlayerCup
) : Cup.PlayerCup by playerCup {

	constructor(
		stones: UInt,
		cupFactory: CupFactory = DefaultCup
	) : this(playerCup = cupFactory.createPlayerCup(
		stones = stones
	))

}