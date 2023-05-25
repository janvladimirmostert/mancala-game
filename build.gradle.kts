val mancalaGameVersion = libs.versions.mancala.game.get()

plugins {
	val kotlinVersion = libs.versions.kotlin.get()
	kotlin("multiplatform").version(kotlinVersion).apply(false)
}

allprojects {
	this.group = "com.mancala"
	this.version = mancalaGameVersion

	this.repositories {
		mavenLocal()
		mavenCentral()
	}

	buildscript {
		repositories {
			mavenLocal()
		}
	}

	tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
		kotlinOptions.freeCompilerArgs += listOf(
			"-Xcontext-receivers",
			"-opt-in=kotlin.time.ExperimentalTime",
			"-opt-in=kotlin.contracts.ExperimentalContracts",
			"-opt-in=kotlin.js.ExperimentalJsExport",
			"-opt-in=kotlin.ExperimentalUnsignedTypes",
		)
	}
}

tasks.withType<Copy> {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


