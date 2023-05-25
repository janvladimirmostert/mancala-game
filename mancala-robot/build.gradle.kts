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

	sourceSets {
		val jvmMain by getting {
			dependencies {
				implementation(project(":mancala-domain"))
			}
		}
		val jvmTest by getting {
			dependencies {
				implementation(libs.bundles.kotlin.test.jvm)
			}
		}
	}
}