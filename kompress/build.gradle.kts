plugins {
    kotlin("multiplatform") version "2.3.0"
}

group = "me.devnatan"
version = "0.1.0"

kotlin {
    explicitApi()
    mingwX64("native")

//    val hostOs = System.getProperty("os.name")
//    val isArm64 = System.getProperty("os.arch") == "aarch64"
//    val isMingwX64 = hostOs.startsWith("Windows")
//    when {
//        hostOs == "Mac OS X" && isArm64 -> macosArm64("native")
//        hostOs == "Mac OS X" && !isArm64 -> macosX64("native")
//        hostOs == "Linux" && isArm64 -> linuxArm64("native")
//        hostOs == "Linux" && !isArm64 -> linuxX64("native")
//        isMingwX64 -> mingwX64("native")
//        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
//    }
//
    sourceSets.configureEach {
        val suffix = "Main"
        val platform = name.dropLast(suffix.length)
        val srcDir = if (name.endsWith(suffix)) "src" else "test"
        kotlin.srcDir("$platform/$srcDir")

        val resourcesFile = if (srcDir == "src") "resources" else "test-resources"
        resources.srcDirs("$platform/$resourcesFile")

        languageSettings {
            progressiveMode = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.3.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("org.apache.commons:commons-compress:1.24.0")
            }
        }

        val nativeMain by getting { dependsOn(commonMain) }
        val appleMain by creating { dependsOn(nativeMain) }
        val macosArm64Main by creating { dependsOn(appleMain) }
    }
}
