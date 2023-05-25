package com.mancala.implementation.game

import com.mancala.domain.cup.CupFactory
import com.mancala.domain.game.GameRules
import com.mancala.implementation.cup.DefaultCup

class GameRulesDelegate(
	private val gameRules: GameRules
) : GameRules by gameRules {

	constructor(
		cupFactory: CupFactory = DefaultCup
	) : this(gameRules = DefaultGameRules(
		cupFactory = cupFactory
	))

}