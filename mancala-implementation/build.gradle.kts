import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

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

	js(IR) {
		browser {}
		binaries.library()
	}

	sourceSets {
		val commonMain by getting {
			dependencies {
				implementation(project(":mancala-domain"))
				implementation(project(":mancala-util"))
			}
		}
		val jvmTest by getting {
			dependencies {
				implementation(libs.bundles.kotlin.test.jvm)
			}
		}

	}
}