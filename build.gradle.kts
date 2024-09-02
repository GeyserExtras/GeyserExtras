plugins {
    java
    id("io.freefair.lombok") version "8.6"
    id("com.gradleup.shadow") version "8.3.0"
}

repositories {
    mavenCentral()

    maven("https://maven.lenni0451.net/snapshots/") {
        name = "lenni0451Maven"
    }
    maven("https://plugins.gradle.org/m2/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://repo.opencollab.dev/main/")
    maven("https://repo.codemc.io/repository/maven-releases/")
}

dependencies {
    implementation("org.spongepowered:configurate-yaml:4.2.0-GeyserMC-SNAPSHOT")
    shadow("org.spongepowered:configurate-yaml:4.2.0-GeyserMC-SNAPSHOT")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")
    shadow("com.fasterxml.jackson.core:jackson-annotations:2.17.0")
    shadow("com.fasterxml.jackson.core:jackson-core:2.17.0")
    shadow("com.fasterxml.jackson.core:jackson-databind:2.17.0")

    compileOnly("org.spigotmc:spigot-api:1.12-R0.1-SNAPSHOT")
    compileOnly("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.3.0-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.21-R0.1-SNAPSHOT")
    compileOnly("org.geysermc.geyser:core:2.4.2-SNAPSHOT")
    compileOnly("org.geysermc.floodgate:core:2.2.3-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    testCompileOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    processResources {
        filesMatching(listOf("plugin.yml", "bungee.yml", "extension.yml")) {
            expand(
                "version" to version,
                "id" to "geyserextras",
                "name" to "GeyserExtras"
            )
        }
    }

    jar {
        dependsOn(shadowJar)
        archiveClassifier.set("unshaded")
    }

    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("GeyserExtras.jar")
        relocate("org.spongepowered", "dev.letsgoaway.relocate.org.spongepowered")
        relocate("io.leangen.geantyref", "dev.letsgoaway.io.leangen.geantyref")
        relocate("com.fasterxml", "dev.letsgoaway.relocate.com.fasterxml")
    }
}
