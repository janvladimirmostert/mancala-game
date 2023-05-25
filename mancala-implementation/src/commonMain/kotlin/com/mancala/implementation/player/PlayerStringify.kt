package com.mancala.implementation.player

import com.mancala.domain.player.Player
import com.mancala.implementation.cup.CupStringify.stringify

object PlayerStringify {

	fun Player.stringify(namePadding: UInt, cupPadding: UInt): String {
		return this.playerCups.joinToString(
			prefix = this.name.padStart(namePadding.toInt()) + " -> ",
			separator = "",
			postfix = "::"
		) {
			it.stringify(cupPadding = cupPadding)
		} + this.scoringCup.stringify(
			cupPadding = cupPadding
		)
	}

}