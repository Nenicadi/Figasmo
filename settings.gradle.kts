pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        gradlePluginPortal()
    }
}

rootProject.name = "Figasmo"

plugins {
    id("dev.kikugie.stonecutter") version "0.9.8"
}

stonecutter {
    create(rootProject) {
        versions(
            "1.21.11", "1.21.10",
            "1.21.8", "1.21.6", "1.21.5",
            "1.21.4"
        )
    }
}