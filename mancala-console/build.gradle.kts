plugins {
	application
	kotlin("multiplatform")
	id("com.github.johnrengelman.shadow")
}

kotlin {
	jvm {
		compilations.all {
			kotlinOptions.jvmTarget = libs.versions.jvm.target.get()
		}
		jvmToolchain {
			languageVersion.set(JavaLanguageVersion.of(libs.versions.jvm.target.get()))
		}
		// only here to make shadowJar work
		withJava()
	}

	sourceSets {
		val jvmMain by getting {
			dependencies {
				implementation(project(":mancala-util"))
				implementation(project(":mancala-domain"))
				implementation(project(":mancala-implementation"))
				implementation(project(":mancala-robot"))
			}
		}
	}
}

application {
	mainClass.set("com.mancala.console.Play")
}