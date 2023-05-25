package com.mancala.domain.game

sealed interface GameState {

	sealed interface Active : GameState {
		object PlayerOneToMove : Active
		object PlayerTwoToMove : Active
	}

	sealed interface Over : GameState {
		object PlayerOneWon : Over
		object PlayerTwoWon : Over
		object Draw : Over
	}

}