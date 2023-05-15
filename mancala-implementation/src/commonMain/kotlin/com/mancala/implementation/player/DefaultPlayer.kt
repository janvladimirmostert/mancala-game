package com.mancala.implementation.player

import com.mancala.domain.cup.Cup
import com.mancala.domain.player.Player
import com.mancala.domain.player.PlayerFactory
import com.mancala.implementation.player.PlayerStringify.stringify

@Suppress("DataClassPrivateConstructor")
data class DefaultPlayer private constructor(
	override val name: String = "DefaultPlayer",
	override val playerCups: List<Cup.PlayerCup>,
	override val scoringCup: Cup.ScoringCup
) : Player {

	override fun toString(): String {
		return this.stringify(
			namePadding = 0u,
			cupPadding = allCups.maxOf(Cup::stones).toString().length.toUInt(),
		)
	}

	companion object : PlayerFactory {

		override fun create(
			name: String,
			playerCups: List<Cup.PlayerCup>,
			scoringCup: Cup.ScoringCup
		): Player {
			return DefaultPlayer(
				name = name,
				playerCups = playerCups,
				scoringCup = scoringCup
			)
		}

	}

}