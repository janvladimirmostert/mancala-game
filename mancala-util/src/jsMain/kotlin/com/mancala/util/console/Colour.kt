package com.mancala.util.console

actual fun String.toColour(vararg colour: COLOUR, reset: Boolean): String {

	// colour is not supported in the browser console without lots of hacks
	// so for the sake of simplicity, we're just returning the String
	// without add colour to it

	return this

}