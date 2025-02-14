rootProject.name = "GeyserExtras"
pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.neoforged.net/releases")
        gradlePluginPortal()
    }
}

include("core", "spigot", "bungee", "velocity", "extension")