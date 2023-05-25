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
		val commonMain by getting
		val jvmMain by getting
		val jvmTest by getting
		val jsMain by getting
	}
}