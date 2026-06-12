plugins {
	alias(libs.plugins.stonecutter)
	alias(libs.plugins.dotenv)
	alias(libs.plugins.fabric.loom).apply(false)
	alias(libs.plugins.neoforged.moddev).apply(false)
	alias(libs.plugins.jsonlang.postprocess).apply(false)
	alias(libs.plugins.mod.publish.plugin).apply(false)
	alias(libs.plugins.kotlin.jvm).apply(false)
	alias(libs.plugins.devtools.ksp).apply(false)
	alias(libs.plugins.fletching.table).apply(false)
}

stonecutter active file(".sc_active_version")

for (version in stonecutter.versions.map { it.version }.distinct()) tasks.register("publish$version") {
	group = "publishing"
	dependsOn(stonecutter.tasks.named("publishMods") { metadata.version == version })
}

stonecutter tasks {
	val ordering = versionComparator.thenComparingInt { task ->
		if (task.metadata.project.endsWith("fabric")) 1 else 0
	}

	listOf("publishModrinth", "publishCurseforge").forEach { taskName ->
		gradle.allprojects {
			if (project.tasks.findByName(taskName) != null) {
				order(taskName, ordering)
			}
		}
	}
}

stonecutter parameters {
	constants.match(node.metadata.project.substringAfterLast('-'), "fabric", "neoforge", "forge")
	filters.include("**/*.fsh", "**/*.vsh")
	swaps["mod_version"] = "\"" + property("mod.version") + "\";"
	swaps["mod_id"] = "\"" + property("mod.id") + "\";"
	swaps["mod_name"] = "\"" + property("mod.name") + "\";"
	swaps["mod_group"] = "\"" + property("mod.group") + "\";"
	swaps["minecraft"] = "\"" + node.metadata.version + "\";"
	constants["release"] = property("mod.id") != "modtemplate"

	replacements {
		filters.exclude("**/*.accesswidener", "**/*.cfg")
		string(current.parsed > "26.1.2") {
			replace("InstantenousMobEffect", "InstantaneousMobEffect")
			replace("applyInstantenousEffect", "applyInstantaneousEffect")
			replace("ContextualBarRenderer", "ContextualBar")
			replace("import net.minecraft.advancements.criterion.EntitySubPredicate;", "import net.minecraft.advancements.predicates.entity.EntitySubPredicate;")
			replace("import net.minecraft.client.gui.Gui;", "import net.minecraft.client.gui.Hud;")
			replace("Gui.HeartType", "Hud.HeartType")
			replace("@Mixin(Gui.class)", "@Mixin(Hud.class)")
		}
	}
}
