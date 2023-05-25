package com.mancala.implementation.player

import com.mancala.domain.cup.CupFactory
import com.mancala.domain.player.Player
import com.mancala.domain.player.PlayerFactory
import com.mancala.implementation.cup.DefaultCup

data class PlayerDelegate(
	private val player: Player,
) : Player by player {

	constructor(
		name: String = "DefaultPlayer",
		cups: UInt,
		stonesPerCup: UInt,
		cupFactory: CupFactory = DefaultCup,
		playerFactory: PlayerFactory = DefaultPlayer
	) : this(player = playerFactory.create(
		name = name,
		cups = cups,
		stonesPerCup = stonesPerCup,
		cupFactory = cupFactory
	))

	constructor(
		name: String = "DefaultPlayer",
		cups: List<UInt>,
		score: UInt,
		cupFactory: CupFactory = DefaultCup,
		playerFactory: PlayerFactory = DefaultPlayer
	) : this(player = playerFactory.create(
		name = name,
		cups = cups,
		score = score,
		cupFactory = cupFactory
	))

}