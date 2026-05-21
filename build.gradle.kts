plugins {
    id("java")
    id("io.freefair.lombok") version "9.5.0"
    id("com.gradleup.shadow") version "9.4.1"
    id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT" apply false
    id("net.neoforged.moddev") version "2.0.141" apply false
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
    maven("https://repo.opencollab.dev/main")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://maven.fabricmc.net/")
    maven("https://maven.architectury.dev/")
    maven("https://maven.neoforged.net/releases")
    maven("https://repo.codemc.io/repository/maven-releases/")

    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    val configurate_yaml: String by project
    val jackson_version: String by project
    val geyser_version: String by project
    val lombok_version: String by project

    implementation("org.spongepowered:configurate-yaml:${configurate_yaml}")
    shadow("org.spongepowered:configurate-yaml:${configurate_yaml}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${jackson_version}")
    implementation("com.fasterxml.jackson.core:jackson-core:${jackson_version}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${jackson_version}")
    shadow("com.fasterxml.jackson.core:jackson-annotations:${jackson_version}")
    shadow("com.fasterxml.jackson.core:jackson-core:${jackson_version}")
    shadow("com.fasterxml.jackson.core:jackson-databind:${jackson_version}")

    compileOnly("org.geysermc.geyser:core:${geyser_version}")

    compileOnly("org.projectlombok:lombok:${lombok_version}")
    annotationProcessor("org.projectlombok:lombok:${lombok_version}")
    testCompileOnly("org.projectlombok:lombok:${lombok_version}")
    testAnnotationProcessor("org.projectlombok:lombok:${lombok_version}")
}

subprojects {
    repositories {
        mavenLocal()
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
            toolchain.languageVersion = JavaLanguageVersion.of(25)
        }
    }

    apply {
        plugin("java")
        plugin("io.freefair.lombok")
        plugin("com.gradleup.shadow")
    }

    tasks {
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

