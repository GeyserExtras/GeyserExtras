plugins {
    id("net.neoforged.moddev")
}

val neoforge_version: String by project
val mod_id: String by project
val mod_name: String by project
val mod_author: String by project
val license: String by project
val description: String by project
val minecraft_version_range: String by project
val neoforge_loader_version_range: String by project

dependencies {
    implementation(project(":core"))
}

neoForge {
    version = project.property("neoforge_version") as String
    // Automatically enable neoforge AccessTransformers if the file exists
    val at = project(":core").file("src/main/resources/META-INF/accesstransformer.cfg")
    if (at.exists()) {
        accessTransformers.from(at.absolutePath)
    }
    runs {
        configureEach {
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
            ideName.set("NeoForge ${this.name.replaceFirstChar { it.uppercase() }} (${project.path})") // Unify the run config names with fabric
        }
        create("client") {
            client()
            gameDirectory.set(file("runs/client").apply { mkdirs() })
        }
        create("data") {
            clientData()
            gameDirectory.set(file("runs/data").apply { mkdirs() })
            // DataGen can be run by - "./gradlew :neoforge:runData" in Terminal.
            // Specify the modid for data generation, where to output the resulting resource, and where to look for existing resources.
            programArguments.addAll("--mod", mod_id, "--all", "--output", file("src/generated/resources/").absolutePath, "--existing", file("src/main/resources/").absolutePath)
        }
        create("server") {
            server()
            gameDirectory.set(file("runs/server").apply { mkdirs() })
        }
    }
    mods {
        create(mod_id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version_range", minecraft_version_range)
    inputs.property("neoforge_loader_version_range", neoforge_loader_version_range)
    inputs.property("mod_id", mod_id)
    inputs.property("mod_name", mod_name)
    inputs.property("mod_author", mod_author)
    inputs.property("license", license)
    inputs.property("description", description)

    filesMatching("neoforge.mods.toml") {
        expand(
            "version" to project.version,
            "minecraft_version_range" to minecraft_version_range,
            "neoforge_loader_version_range" to neoforge_loader_version_range,
            "mod_id" to mod_id,
            "mod_name" to mod_name,
            "mod_author" to mod_author,
            "license" to license,
            "description" to (description ?: "")
        )
    }
}

sourceSets.main.get().resources { srcDir("src/generated/resources") }

// Implement mcgradleconventions loader attribute
val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
listOf("apiElements", "runtimeElements").forEach { variant ->
    configurations.named(variant) {
        attributes {
            attribute(loaderAttribute, "neoforge")
        }
    }
}
sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName).forEach { variant ->
        configurations.named(variant) {
            attributes {
                attribute(loaderAttribute, "neoforge")
            }
        }
    }
}
