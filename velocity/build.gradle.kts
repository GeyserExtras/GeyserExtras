
dependencies {
    val packetevents_version: String by project
    val geyser_version: String by project
    val velocity_version: String by project

    implementation(project(":core"))
    compileOnly("org.geysermc.geyser:core:${geyser_version}")
    compileOnly("com.velocitypowered:velocity-api:${velocity_version}")
    annotationProcessor("com.velocitypowered:velocity-api:${velocity_version}")
    compileOnly("com.github.retrooper:packetevents-velocity:${packetevents_version}")
}


tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("GeyserExtras-Velocity.jar")
        relocate("org.spongepowered", "dev.letsgoaway.relocate.org.spongepowered")
        relocate("io.leangen.geantyref", "dev.letsgoaway.io.leangen.geantyref")
        relocate("com.fasterxml", "dev.letsgoaway.relocate.com.fasterxml")
        relocate("it.unimi.dsi.fastutil", "org.geysermc.geyser.platform.velocity.shaded.it.unimi.dsi.fastutil")
    }
}
