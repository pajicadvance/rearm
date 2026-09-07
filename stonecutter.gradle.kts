import java.util.Locale

plugins {
    id("dev.kikugie.stonecutter")
    kotlin("jvm") apply false
    id("com.google.devtools.ksp") apply false
    id("dev.kikugie.fletching-table.fabric") apply false
    id("me.modmuss50.mod-publish-plugin") apply false
}

stonecutter active "26.2-fabric"

stonecutter parameters {
    val (version, loader) = current.project.split('-', limit = 2)
    val versionFormatted = version.replace(".", "_")
    val loaderFormatted = loader.replaceFirstChar { it.uppercase(Locale.getDefault()) }
    val modId = properties.get<String>("mod.id")
    val modGroup = properties.get<String>("mod.group")

    properties {
        tags(version, loader)
    }

    constants {
        match(loader, "fabric", "neoforge")
    }

    swaps["mod_id"] = "\"${modId}\";"
    swaps["version_util_import"] = "import ${modGroup}.${modId}.platform.version.Util${versionFormatted};"
    swaps["version_util_inst"] = "new Util${versionFormatted}();"
    swaps["loader_util_import"] = "import ${modGroup}.${modId}.platform.${loader}.${loaderFormatted}LoaderUtil;"
    swaps["loader_util_inst"] = "new ${loaderFormatted}LoaderUtil();"
    constants["release"] = properties.get<String>("mod.id") != "template"
    dependencies["fapi"] = properties.getOrNull<String>("deps.fabric_api") ?: "0"

    replacements {
        filters.exclude("**/*.ct")
        string(current.parsed >= "1.21.11") {
            replace("ValidatedIdentifier", "ValidatedIdentifier")
            replace("ResourceLocation", "Identifier")
            replace("GuiGraphics", "GuiGraphicsExtractor")
            replace("net.ramixin.mixson_backport", "net.ramixin.mixson")
            replace("net.minecraft.world.entity.projectile.AbstractArrow", "net.minecraft.world.entity.projectile.arrow.AbstractArrow")
            replace("net.minecraft.client.model.ShieldModel", "net.minecraft.client.model.object.equipment.ShieldModel")
            replace("net.minecraft.world.entity.projectile.DragonFireball", "net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball")
            replace("playS2C()", "clientboundPlay()")
            replace("playC2S()", "serverboundPlay()")
            replace("ItemGroupEvents.modifyEntriesEvent", "CreativeModeTabEvents.modifyOutputEvent")
            replace("import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;", "import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;")
        }
        string(current.parsed > "26.1.2") {
            replace("InstantenousMobEffect", "InstantaneousMobEffect")
            replace("applyInstantenousEffect", "applyInstantaneousEffect")
            replace("ContextualBarRenderer", "ContextualBar")
            replace("import net.minecraft.client.gui.Gui;", "import net.minecraft.client.gui.Hud;")
            replace("Gui.HeartType", "Hud.HeartType")
            replace("@Mixin(Gui.class)", "@Mixin(Hud.class)")
        }
    }
}
