package com.mancala.implementation.game

import com.mancala.domain.cup.Cup
import com.mancala.domain.game.Game
import com.mancala.domain.player.Player
import com.mancala.implementation.cup.CupStringify.stringify
import com.mancala.util.console.COLOUR
import com.mancala.util.console.toColour

object GameStringify {

	private fun generateBoard(
		player: Player,
		opponent: Player
	): String {

		val namePadding = listOf(
			player.name,
			opponent.name
		).maxOf(String::length).let {
			if (it < 4) {
				4
			} else {
				it
			}
		}

		val cupPadding = listOf(
			*player.allCups.toTypedArray(),
			*opponent.allCups.toTypedArray()
		).maxOf(Cup::stones).toString().length

		return listOf(
			"=".repeat((namePadding * 2) + (cupPadding + 2) * opponent.allCups.size + 5),
			listOf(
				" ".repeat(namePadding - 1),
				opponent.scoringCup.stringify(cupPadding = cupPadding.toUInt()),
				"::",
				*opponent.playerCups.asReversed().map {
					it.stringify(cupPadding = cupPadding.toUInt())
				}.toTypedArray(),
				" <- ",
				opponent.name.padEnd(namePadding)
			).joinToString(separator = ""),
			listOf(
				player.name.padStart(namePadding).toColour(COLOUR.ANSI_PURPLE),
				" -> ",
				*player.playerCups.map {
					it.stringify(cupPadding = cupPadding.toUInt())
				}.toTypedArray(),
				"::",
				player.scoringCup.stringify(cupPadding = cupPadding.toUInt())
			).joinToString(separator = ""),
			"=".repeat((namePadding * 2) + (cupPadding + 2) * player.allCups.size + 5),
		).joinToString("\n")
	}

	fun Game.stringify(): String {
		return if (this.player2Turn) {
			generateBoard(player = player2, opponent = player1)
		} else {
			generateBoard(player = player1, opponent = player2)
		}
	}

}