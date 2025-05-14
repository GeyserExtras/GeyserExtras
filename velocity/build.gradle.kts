
dependencies {
    implementation(project(":core"))

    compileOnly("org.geysermc.geyser:core:2.7.1-SNAPSHOT")

    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
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
