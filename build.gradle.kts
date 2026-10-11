plugins {
    kotlin("jvm") version "2.4.20"
    kotlin("plugin.lombok") version "2.4.21"
    id("fabric-loom") version "1.17-SNAPSHOT"
}

repositories {
    maven("https://maven.figuramc.org/releases")
    maven("https://maven.figuramc.org/snapshots")
    maven("https://penguinencounter.github.io/mvn/snapshots")
    maven("https://repo.plasmoverse.com/releases")
    maven("https://maven.maxhenkel.de/repository/public")
    maven {
        url = uri("https://jitpack.io")
        content {
            includeGroup("com.github.FiguraMC.luaj")
        }
    }
    mavenCentral()
}

version = "${project.property("mod.version")}+${sc.current.version}"
base.archivesName = rootProject.name

val jdkVersion: Int = 21
val figuraApi = if (file("libs").exists())
    files("libs/figura.jar")
    else "org.figuramc:figura-fabric:${project.property("figura.version")}"

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:${project.property("fabric.loader")}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.property("fabric.kotlin_loader_version")}")

    modImplementation(figuraApi)
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.property("fabric.version")}")

    implementation("com.github.FiguraMC.luaj:luaj-core:${project.property("figura.luaj")}-figura")
    implementation("com.github.FiguraMC.luaj:luaj-jse:${project.property("figura.luaj")}-figura")

    compileOnly("su.plo.voice.api:client:${project.property("plasmovoice.version")}")
    compileOnly("de.maxhenkel.voicechat:voicechat-api:${project.property("voicechat.version")}")
}

tasks.processResources {
    val props = mapOf(
        "version" to version,
        "minecraft_version" to sc.current.version,
        "loader_version" to project.property("fabric.loader"),
        "kotlin_loader_version" to project.property("fabric.kotlin_loader_version"),
        "figura_version" to project.property("figura.version")
    )

    listOf("fabric.mod.json", "Figasmo.kt").forEach {
        filesMatching(it) {
            expand(props)
        }
    }

}

kotlin {
    jvmToolchain(jdkVersion)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(jdkVersion)
}