plugins {
    id("java-library")
}

dependencies {
    val configurate_yaml: String by project
    val jackson_version: String by project
    val packetevents_version: String by project
    val geyser_version: String by project
    val floodgate_version: String by project
    val lombok_version: String by project
    val mixin_version: String by project
    val mixinextras_version: String by project

    implementation("org.spongepowered:configurate-yaml:$configurate_yaml")

    implementation("com.fasterxml.jackson.core:jackson-annotations:$jackson_version")
    implementation("com.fasterxml.jackson.core:jackson-core:$jackson_version")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jackson_version")

    compileOnly("com.github.retrooper:packetevents-api:$packetevents_version")
    compileOnly("org.geysermc.geyser:core:$geyser_version")
    compileOnly("org.geysermc.floodgate:core:$floodgate_version")

    compileOnly("org.spongepowered:mixin:$mixin_version")
    compileOnly("io.github.llamalad7:mixinextras-common:$mixinextras_version")

    compileOnly("org.projectlombok:lombok:$lombok_version")
    annotationProcessor("org.projectlombok:lombok:$lombok_version")

    testCompileOnly("org.projectlombok:lombok:$lombok_version")
    testAnnotationProcessor("org.projectlombok:lombok:$lombok_version")
}

configurations {
    create("commonJava") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
    create("commonResources") {
        isCanBeResolved = false
        isCanBeConsumed = true
    }
}

artifacts {
    add("commonJava", sourceSets.main.get().java.sourceDirectories.singleFile)
    add("commonResources", sourceSets.main.get().resources.sourceDirectories.singleFile)
}

// Implement mcgradleconventions loader attribute
val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
listOf("apiElements", "runtimeElements").forEach { variant ->
    configurations.named(variant) {
        attributes {
            attribute(loaderAttribute, "common")
        }
    }
}
sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName).forEach { variant ->
        configurations.named(variant) {
            attributes {
                attribute(loaderAttribute, "common")
            }
        }
    }
}


tasks {
    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("GeyserExtras.jar")
        relocate("org.spongepowered", "dev.letsgoaway.relocate.org.spongepowered")
        relocate("io.leangen.geantyref", "dev.letsgoaway.io.leangen.geantyref")
        relocate("com.fasterxml", "dev.letsgoaway.relocate.com.fasterxml")

    }
}

