package com.mancala.domain.game

import com.mancala.domain.cup.Cup
import com.mancala.domain.cup.CupFactory
import com.mancala.domain.player.Player
import com.mancala.domain.player.PlayerFactory

interface GameFactory {

	fun create(
		player1: Player,
		player2: Player,
		state: GameState,
		rules: GameRules,
		cupFactory: CupFactory,
		playerFactory: PlayerFactory,
	): Game

	fun create(
		player1Name: String = "Player 1",
		player2Name: String = "Player 2",
		cups: UInt,
		stonesPerCup: UInt,
		cupFactory: CupFactory,
		playerFactory: PlayerFactory
	): Game

	fun create(
		player1Name: String = "Player 1",
		player1Cups: List<UInt>,
		player1ScoringCup: UInt,
		player2Name: String = "Player 2",
		player2Cups: List<UInt>,
		player2ScoringCup: UInt,
		cupFactory: CupFactory,
		playerFactory: PlayerFactory,
		gameState: GameState
	): Game

}