package com.mancala.util.console

actual fun String.toColour(vararg colour: COLOUR, reset: Boolean): String {

	var result = ""

	colour.forEach {
		result += it.color
	}
	result += this
	if (reset) {
		result += COLOUR.ANSI_RESET.color
	}

	return result

}