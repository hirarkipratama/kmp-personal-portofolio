import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":shared"))

            implementation(libs.compose.ui)
        }
    }
}

val copyContentToDistribution by tasks.registering(Copy::class) {
    from(project.rootDir.resolve("content"))
    into(layout.buildDirectory.dir("dist/wasmJs/productionExecutable/content"))
}

val copyPublicToDistribution by tasks.registering(Copy::class) {
    from(project.rootDir.resolve("public"))
    into(layout.buildDirectory.dir("dist/wasmJs/productionExecutable"))
    exclude("**/.gitkeep")
}

tasks.named("wasmJsBrowserDistribution") {
    dependsOn(copyContentToDistribution, copyPublicToDistribution)
}

tasks.named("wasmJsBrowserProductionWebpack") {
    finalizedBy(copyContentToDistribution, copyPublicToDistribution)
}