package com.mancala.implementation.cup

import com.mancala.domain.cup.Cup
import com.mancala.util.console.COLOUR
import com.mancala.util.console.toColour

object CupStringify {

	fun Cup.stringify(cupPadding: UInt): String {
		return "[" + this.stones.toString().padStart(
			length = cupPadding.toInt(),
			padChar = '0'
		).toColour(COLOUR.ANSI_BLUE) + "]"
	}

}