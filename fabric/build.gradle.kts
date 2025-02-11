plugins {
    id("fabric-loom") version "1.10-SNAPSHOT"
}

base {
    archivesName = "GeyserExtras-Fabric"
}

loom {
    splitEnvironmentSourceSets()

    mods {
        sourceSets["main"]
        sourceSets["client"]
    }
}

repositories {
// Add repositories to retrieve artifacts from in here.
// You should only use this when depending on other mods because
// Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
// See https://docs.gradle.org/current/userguide/declaring_repositories.html
// for more information about repositories.
}

dependencies {
    implementation(project(":core"))
// To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")

    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")
    modApi("net.fabricmc.fabric-api:fabric-api:${project.properties["fabric_version"]}")
}


tasks {
    processResources {
        filesMatching("fabric.mod.json") {
            expand("version" to project.version,
                    "minecraft_version" to project.properties["minecraft_version"],
                    "loader_version" to project.properties["loader_version"]
            )
        }
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(zipTree(project.project(":core").tasks.jar.get().archiveFile))
    }

    remapJar {
        archiveClassifier.set("")
        archiveFileName.set("GeyserExtras-Fabric.jar")
    }

    register("prepareKotlinBuildScriptModel"){

    }



    shadowJar {
        enabled = false
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        archiveClassifier.set("")
        archiveFileName.set("GeyserExtras-Fabric.jar")
        from(zipTree(project.project(":core").tasks.jar.get().archiveFile))
    }
}


