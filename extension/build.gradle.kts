dependencies {
    val geyser_version: String by project
    val bungeecord_version: String by project

    implementation(project(":core"))

    compileOnly("org.geysermc.geyser:core:${geyser_version}")
    compileOnly("net.md-5:bungeecord-api:${bungeecord_version}")
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("GeyserExtras-Extension.jar")
        relocate("org.spongepowered", "dev.letsgoaway.relocate.org.spongepowered")
        relocate("io.leangen.geantyref", "dev.letsgoaway.io.leangen.geantyref")
        relocate("com.fasterxml", "dev.letsgoaway.relocate.com.fasterxml")
    }
}
