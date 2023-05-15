rootProject.name = "mancala-game"

// internals to make the game work
include("mancala-util")
include("mancala-domain")
include("mancala-implementation")
include("mancala-robot")

// play the game
include("mancala-console")
include("mancala-web")

pluginManagement {
	repositories {
		mavenLocal()
		mavenCentral()
		gradlePluginPortal()
	}
}