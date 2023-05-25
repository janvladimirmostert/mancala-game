package com.mancala.util.console

object Read {

	fun readlnUntilNotEmpty(): String {
		var input = readln().trim()
		while (input.isEmpty()) {
			input = readln().trim()
		}
		return input
	}

	fun readUIntOrNull(): UInt {
		var input = readln().trim().toUIntOrNull()
		while (input == null) {
			input = readln().trim().toUIntOrNull()
		}
		return input
	}

}