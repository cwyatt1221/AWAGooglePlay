plugins {
    // AGP 9.x has built-in Kotlin support (android.builtInKotlin, enabled by
    // default). The standalone org.jetbrains.kotlin.android plugin is NOT
    // compatible with the AGP 9 DSL, so it is intentionally not declared.
    id("com.android.application") version "9.3.0" apply false
}
