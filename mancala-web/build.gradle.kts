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
		moduleName = "mancala"
		binaries.library()
		browser {
			testTask {
				enabled = false
				// useKarma {
				// useChromeHeadless()
				// useFirefox()
				// useFirefoxHeadless()
				// useChrome()
				//}
			}
		}
		generateTypeScriptDefinitions()
	}

	sourceSets {
		val jsMain by getting {
			dependencies {
				implementation(project(":mancala-domain"))
				implementation(project(":mancala-implementation"))
			}
		}
	}

	// TODO: get ES modules working with ESBuild
	//	tasks.withType<KotlinJsCompile>().configureEach {
	//		kotlinOptions {
	//			moduleKind = "es"
	//			useEsClasses = true
	//		}
	//	}

}