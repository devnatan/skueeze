import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform") version "2.3.0"
    id("org.jetbrains.kotlin.plugin.allopen") version "2.3.0"
    id("org.jetbrains.kotlinx.benchmark") version "0.4.15"
}

group = "me.devnatan"
version = "0.1.0"

kotlin {
    explicitApi()
    mingwX64()

    targets.withType<KotlinNativeTarget> {
        val benchmark by compilations.creating {
            associateWith(this@withType.compilations.getByName("main"))
        }
    }

    sourceSets.configureEach {
        val sourceSetName = name

        when (sourceSetName) {
            "commonMain" -> {
                kotlin.srcDir("common/src")
                resources.srcDirs("common/resources")
            }
            "commonTest" -> {
                kotlin.srcDir("common/test")
                resources.srcDirs("common/test-resources")
            }
            "commonBenchmark" -> {
                kotlin.srcDir("common/benchmarks")
                resources.srcDir("common/benchmarks-resources")
            }
        }

        languageSettings {
            progressiveMode = false
        }
    }

    sourceSets {
        val commonMain by getting {
        }

        val commonBenchmark by creating {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.15")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

benchmark {
    targets {
         register("mingwX64Benchmark")
    }

    configurations {
        named("main") {
            warmups = 5
            iterations = 10
            iterationTime = 1
            iterationTimeUnit = "s"
            reportFormat = "json"
        }

        register("fast") {
            warmups = 2
            iterations = 5
            iterationTime = 500
            iterationTimeUnit = "ms"
            reportFormat = "text"
        }

        register("thorough") {
            warmups = 10
            iterations = 20
            iterationTime = 2
            iterationTimeUnit = "s"
            reportFormat = "json"
        }
    }
}
