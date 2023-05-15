plugins {
	kotlin("multiplatform")
}

kotlin {
	jvm {
		compilations.all {
			kotlinOptions.jvmTarget = libs.versions.jvm.target.get()
		}
		jvmToolchain {
			languageVersion.set(JavaLanguageVersion.of(libs.versions.jvm.target.get()))
		}
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
