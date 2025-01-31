
dependencies {
    implementation(project(":core"))

    compileOnly("org.geysermc.geyser:core:2.6.0-SNAPSHOT")

    compileOnly("org.spigotmc:spigot-api:1.12-R0.1-SNAPSHOT")
}


tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("GeyserExtras-Spigot.jar")
        relocate("org.spongepowered", "dev.letsgoaway.relocate.org.spongepowered")
        relocate("io.leangen.geantyref", "dev.letsgoaway.io.leangen.geantyref")
        relocate("com.fasterxml", "dev.letsgoaway.relocate.com.fasterxml")
    }
}
