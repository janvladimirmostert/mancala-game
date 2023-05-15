package com.mancala.implementation.game

import com.mancala.domain.cup.CupFactory
import com.mancala.domain.game.Game
import com.mancala.domain.game.GameFactory
import com.mancala.domain.game.GameState
import com.mancala.domain.player.PlayerFactory
import com.mancala.implementation.cup.DefaultCup
import com.mancala.implementation.player.DefaultPlayer

class GameDelegate(
	private val game: Game,
) : Game by game {

	constructor(
		player1Name: String = "Player 1",
		player2Name: String = "Player 2",
		cups: UInt,
		stonesPerCup: UInt,
		cupFactory: CupFactory = DefaultCup,
		playerFactory: PlayerFactory = DefaultPlayer,
		gameFactory: GameFactory = DefaultGame
	) : this(
		gameFactory.create(
			cups = cups,
			stonesPerCup = stonesPerCup,
			player1Name = player1Name,
			player2Name = player2Name,
			cupFactory = cupFactory,
			playerFactory = playerFactory
		)
	)

	constructor(
		player1Name: String = "Player 1",
		player1Cups: List<UInt>,
		player1ScoringCup: UInt,
		player2Name: String = "Player 2",
		player2Cups: List<UInt>,
		player2ScoringCup: UInt,
		state: GameState = GameState.Active.PlayerOneToMove,
		cupFactory: CupFactory = DefaultCup,
		playerFactory: PlayerFactory = DefaultPlayer,
		gameFactory: GameFactory = DefaultGame
	) : this(
		gameFactory.create(
			player1Name = player1Name,
			player1Cups = player1Cups,
			player1ScoringCup = player1ScoringCup,

			player2Name = player2Name,
			player2Cups = player2Cups,
			player2ScoringCup = player2ScoringCup,

			cupFactory = cupFactory,
			playerFactory = playerFactory,

			gameState = state
		).also {
			if (it.player1.playerCups.size != it.player2.playerCups.size) {
				throw IllegalStateException("Player 1 and Player 2 have a different number of player cups")
			}
		}
	)

}