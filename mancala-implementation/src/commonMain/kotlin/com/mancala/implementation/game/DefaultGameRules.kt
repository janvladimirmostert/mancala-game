package com.mancala.implementation.game

import com.mancala.domain.cup.Cup
import com.mancala.domain.cup.CupFactory
import com.mancala.domain.game.GameRules
import com.mancala.domain.player.Player

class DefaultGameRules(
	private val cupFactory: CupFactory
) : GameRules {

	override fun calculateStoneMovement(cup: UInt, player: Player, opponent: Player): GameRules.StoneMovementResult {

		var currentIndex = cup.toInt()
		val getOpponentIndex: () -> Int = {
			opponent.playerCups.lastIndex - currentIndex
		}

		if (player.playerCups.size != opponent.playerCups.size) {
			throw IllegalStateException(
				""" ${player.playerCups.size} cups for ${player.name} and
					${opponent.playerCups.size} cups for ${opponent.name} is 
				    not a valid state
				""".trimIndent()
			)
		}

		// validate that the selected cup is within bounds and that there are stones to move
		if (currentIndex > player.playerCups.lastIndex) {
			return GameRules.StoneMovementResult.IllegalMoveError
		} else if (player.playerCups[currentIndex].stones == 0u) {
			return GameRules.StoneMovementResult.NoStonesToMoveError
		}

		// copy the state of stones in cups so we can calculate a new state
		val playerStones = player.playerCups.map(Cup::stones).toTypedArray()
		val playerScore = player.scoringCup.stones.let { arrayOf(it) }
		val opponentStones = opponent.playerCups.map(Cup::stones).toTypedArray()
		val opponentScore = opponent.scoringCup.stones.let { arrayOf(it) }

		var lastMoveOnPlayerEmptyCup = false
		var lastMoveOnPlayerScoringCup = false
		var gameOver = false

		// move stones
		var cups = mutableListOf(playerStones, playerScore, opponentStones)
		val grabbedStones = playerStones[currentIndex].toInt()
		playerStones[currentIndex] = 0u
		(1..grabbedStones).forEach { index ->
			currentIndex++
			if (currentIndex > cups.first().lastIndex) {
				cups = cups.also {
					it.add(it.removeAt(0))
				}
				currentIndex = 0
			}

			// handle rules around last stone move
			if (index == grabbedStones) {
				cups.first().also {
					if (it === playerStones) {
						lastMoveOnPlayerEmptyCup =
							it[currentIndex] == 0u && opponentStones[getOpponentIndex()] > 0u
					} else if (it === playerScore) {
						lastMoveOnPlayerScoringCup = true
					}
				}

			}

			cups.first()[currentIndex]++

			// capture both own and opponent pieces when lastMoveOnPlayerEmptyCup
			if (lastMoveOnPlayerEmptyCup) {
				playerScore[0] += playerStones[currentIndex]
				playerStones[currentIndex] = 0u
				playerScore[0] += opponentStones[getOpponentIndex()]
				opponentStones[getOpponentIndex()] = 0u
			}
		}

		// handle case where either player is out of stones
		val playerStonesSum = playerStones.sum()
		val opponentStonesSum = opponentStones.sum()
		if (playerStonesSum == 0u || opponentStonesSum == 0u) {
			playerScore[0] += playerStonesSum
			opponentScore[0] += opponentStonesSum
			playerStones.forEachIndexed { index, _ ->
				playerStones[index] = 0u
				opponentStones[index] = 0u
			}
			gameOver = true
		}

		// output result
		return GameRules.StoneMovementResult.Success(
			playerCups = playerStones.map {
				cupFactory.createPlayerCup(it)
			},
			playerScoringCup = playerScore.first().let {
				cupFactory.createScoringCup(it)
			},
			opponentCups = opponentStones.map {
				cupFactory.createPlayerCup(it)
			},
			opponentScoringCup = opponentScore.first().let {
				cupFactory.createScoringCup(it)
			},

			gameOver = gameOver,
			lastMoveOnPlayerEmptyCup = lastMoveOnPlayerEmptyCup,
			lastMoveOnPlayerScoringCup = lastMoveOnPlayerScoringCup,
			playerGetsAnotherMove = !gameOver && lastMoveOnPlayerScoringCup,
			playerWon = gameOver && playerScore[0] > opponentScore[0],
			opponentWon = gameOver && playerScore[0] < opponentScore[0],
			gameDrew = gameOver && playerScore[0] == opponentScore[0],
		)

	}


}