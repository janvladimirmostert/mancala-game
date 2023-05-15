package com.mancala.implementation.game

import com.mancala.domain.cup.CupFactory
import com.mancala.domain.game.Game
import com.mancala.domain.game.Game.MoveResult
import com.mancala.domain.game.GameFactory
import com.mancala.domain.game.GameRules
import com.mancala.domain.game.GameState
import com.mancala.domain.player.Player
import com.mancala.domain.player.PlayerFactory
import com.mancala.implementation.game.GameStringify.stringify

@Suppress("DataClassPrivateConstructor")
data class DefaultGame private constructor(
	override val player1: Player,
	override val player2: Player,
	override val state: GameState,
	override val rules: GameRules,
	private val cupFactory: CupFactory,
	private val playerFactory: PlayerFactory,
) : Game {

	override val legalMoves: List<UInt>
		get() = playerTurn?.playerCups?.mapIndexedNotNull { index, playerCup ->
			if (playerCup.stones > 0u) {
				index.toUInt()
			} else {
				null
			}
		} ?: emptyList()

	override val playerTurn: Player?
		get() = if (player1Turn) {
			player1
		} else if (player2Turn) {
			player2
		} else {
			null
		}

	override fun move(cup: UInt): MoveResult {
		return when (state) {
			GameState.Active.PlayerOneToMove -> {
				when (val result = rules.calculateStoneMovement(
					cup = cup, player = player1, opponent = player2
				)) {
					is GameRules.StoneMovementResult.IllegalMoveError -> MoveResult.IllegalMoveError
					is GameRules.StoneMovementResult.NoStonesToMoveError -> MoveResult.NoStonesToMoveError
					is GameRules.StoneMovementResult.Success -> if (result.gameOver) {
						MoveResult.GameOver(mapResultToGameForPlayer1(result))
					} else {
						MoveResult.Success(mapResultToGameForPlayer1(result))
					}
				}
			}

			GameState.Active.PlayerTwoToMove -> {
				when (val result = rules.calculateStoneMovement(
					cup = cup, player = player2, opponent = player1
				)) {
					is GameRules.StoneMovementResult.IllegalMoveError -> MoveResult.IllegalMoveError
					is GameRules.StoneMovementResult.NoStonesToMoveError -> MoveResult.NoStonesToMoveError
					is GameRules.StoneMovementResult.Success -> if (result.gameOver) {
						MoveResult.GameOver(mapResultToGameForPlayer2(result))
					} else {
						MoveResult.Success(mapResultToGameForPlayer2(result))
					}

				}
			}

			GameState.Over.Draw -> MoveResult.GameOver(this)
			GameState.Over.PlayerOneWon -> MoveResult.GameOver(this)
			GameState.Over.PlayerTwoWon -> MoveResult.GameOver(this)
		}
	}

	private fun mapResultToGameForPlayer1(result: GameRules.StoneMovementResult.Success): Game {
		return this.copy(
			player1 = playerFactory.create(
				name = player1.name,
				playerCups = result.playerCups.map {
					cupFactory.createPlayerCup(it.stones)
				},
				scoringCup = result.playerScoringCup.let {
					cupFactory.createScoringCup(it.stones)
				}
			),
			player2 = playerFactory.create(
				name = player2.name,
				playerCups = result.opponentCups.map {
					cupFactory.createPlayerCup(it.stones)
				},
				scoringCup = result.opponentScoringCup.let {
					cupFactory.createScoringCup(it.stones)
				}
			),
			state = if (result.gameOver) {
				if (result.gameDrew) {
					GameState.Over.Draw
				} else if (result.playerWon) {
					GameState.Over.PlayerOneWon
				} else {
					GameState.Over.PlayerTwoWon
				}
			} else if (result.playerGetsAnotherMove) {
				GameState.Active.PlayerOneToMove
			} else {
				GameState.Active.PlayerTwoToMove
			}
		)
	}

	private fun mapResultToGameForPlayer2(result: GameRules.StoneMovementResult.Success): Game {
		return this.copy(
			player2 = playerFactory.create(
				name = player2.name,
				playerCups = result.playerCups.map {
					cupFactory.createPlayerCup(it.stones)
				},
				scoringCup = result.playerScoringCup.let {
					cupFactory.createScoringCup(it.stones)
				}
			),
			player1 = playerFactory.create(
				name = player1.name,
				playerCups = result.opponentCups.map {
					cupFactory.createPlayerCup(it.stones)
				},
				scoringCup = result.opponentScoringCup.let {
					cupFactory.createScoringCup(it.stones)
				}
			),
			state = if (result.gameOver) {
				if (result.gameDrew) {
					GameState.Over.Draw
				} else if (result.playerWon) {
					GameState.Over.PlayerTwoWon
				} else {
					GameState.Over.PlayerOneWon
				}
			} else if (result.playerGetsAnotherMove) {
				GameState.Active.PlayerTwoToMove
			} else {
				GameState.Active.PlayerOneToMove
			}
		)
	}

	override fun toString(): String {
		return this.stringify()
	}

	companion object : GameFactory {

		override fun create(
			player1: Player,
			player2: Player,
			state: GameState,
			rules: GameRules,
			cupFactory: CupFactory,
			playerFactory: PlayerFactory,
		): Game {
			return DefaultGame(
				cupFactory = cupFactory,
				playerFactory = playerFactory,
				player1 = player1,
				player2 = player2,
				state = state,
				rules = rules,
			)
		}

		override fun create(
			player1Name: String,
			player2Name: String,
			cups: UInt,
			stonesPerCup: UInt,
			cupFactory: CupFactory,
			playerFactory: PlayerFactory
		): Game {
			return DefaultGame(
				cupFactory = cupFactory,
				playerFactory = playerFactory,
				player1 = playerFactory.create(
					name = player1Name,
					cups = cups,
					stonesPerCup = stonesPerCup,
					cupFactory = cupFactory
				),
				player2 = playerFactory.create(
					name = player2Name,
					cups = cups,
					stonesPerCup = stonesPerCup,
					cupFactory = cupFactory
				),
				state = GameState.Active.PlayerOneToMove,
				rules = DefaultGameRules(
					cupFactory = cupFactory
				),
			)
		}

		override fun create(
			player1Name: String,
			player1Cups: List<UInt>,
			player1ScoringCup: UInt,
			player2Name: String,
			player2Cups: List<UInt>,
			player2ScoringCup: UInt,
			cupFactory: CupFactory,
			playerFactory: PlayerFactory,
			gameState: GameState
		): Game {
			return DefaultGame(
				player1 = playerFactory.create(
					name = player1Name,
					playerCups = player1Cups.map {
						cupFactory.createPlayerCup(stones = it)
					},
					scoringCup = cupFactory.createScoringCup(
						stones = player1ScoringCup
					)
				),
				player2 = playerFactory.create(
					name = player2Name,
					playerCups = player2Cups.map {
						cupFactory.createPlayerCup(stones = it)
					},
					scoringCup = cupFactory.createScoringCup(
						stones = player2ScoringCup
					)
				),
				state = gameState,
				cupFactory = cupFactory,
				playerFactory = playerFactory,
				rules = DefaultGameRules(
					cupFactory = cupFactory
				),
			)
		}

	}

}