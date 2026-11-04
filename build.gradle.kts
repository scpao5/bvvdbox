plugins {
    alias(libs.plugins.agp.app) apply false
    alias(libs.plugins.lsplugin.apksign) apply false
}

val androidMinSdkVersion by extra(29)
val androidTargetSdkVersion by extra(34)
val androidCompileSdkVersion by extra(37)
val androidCompileSdkVersionMinor by extra(0)
val androidBuildToolsVersion by extra("37.0.0")
val androidSourceCompatibility by extra(JavaVersion.VERSION_21)
val androidTargetCompatibility by extra(JavaVersion.VERSION_21)
val boxVersionCode by extra(4)
val boxVersionName by extra("1.1")
