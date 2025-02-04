plugins {
    id("java")
    id("io.freefair.lombok") version "8.6"
    id("com.gradleup.shadow") version "8.3.0"
}

repositories {
    mavenCentral()
    gradlePluginPortal()

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
    maven("https://maven.fabricmc.net/")
    maven("https://maven.architectury.dev/")
    maven("https://maven.neoforged.net/releases")
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

    compileOnly("org.geysermc.geyser:core:2.6.0-SNAPSHOT")
    compileOnly("org.geysermc.floodgate:core:2.2.3-SNAPSHOT")
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    testCompileOnly("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")
}



subprojects {

    repositories {
        mavenCentral()
        gradlePluginPortal()

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
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.neoforged.net/releases")
    }

    plugins.withId("java") {
        java {
            toolchain.languageVersion = JavaLanguageVersion.of(17)
        }
    }

    apply {
        plugin("java")
        plugin("io.freefair.lombok")
        plugin("com.gradleup.shadow")
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
    }
}

