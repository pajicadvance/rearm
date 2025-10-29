package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.Main;
import me.pajic.rearm.config.ArmorMaterialHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//? if 1.21.1 {
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import java.util.List;
//?} else {
/*import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
*///?}

import java.util.Arrays;
import java.util.Map;

@Mixin(ArmorMaterial.class)
public class ArmorMaterialMixin {

    @Shadow @Final @Mutable
    private float knockbackResistance;

    @Shadow @Final @Mutable
    private float toughness;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void modifyMaterial(
            CallbackInfo ci,
            @Local(argsOnly = true) Map</*? if 1.21.1 {*/ArmorItem.Type/*?} else {*//*ArmorType*//*?}*/, Integer> map,
            @Local(argsOnly = true) /*? if 1.21.1 {*/List<ArmorMaterial.Layer>/*?} else {*//*ResourceKey<EquipmentAsset>*//*?}*/ id
    ) {
        ResourceLocation rl = /*? if 1.21.1 {*/id.getFirst().assetName/*?} else {*//*id.location()*//*?}*/;
        ArmorMaterialHelper.add(rl);
        if (Main.CONFIG.armor.armorRebalance.get()) {
            int targetTotal = 0;
            if (Main.CONFIG.armor.totalArmorOverrides.get().containsKey(rl)) {
                targetTotal = Main.CONFIG.armor.totalArmorOverrides.get().get(rl);
            } else for (Map.Entry</*? if 1.21.1 {*/ArmorItem.Type/*?} else {*//*ArmorType*//*?}*/, Integer> entry : map.entrySet()) {
                if (entry.getKey() != /*? if 1.21.1 {*/ArmorItem.Type/*?} else {*//*ArmorType*//*?}*/.BODY) targetTotal += (int) (entry.getValue() * Main.CONFIG.armor.armorMultiplier.get());
            }
            int[] values = {
                    Math.round(targetTotal * ((float) Main.CONFIG.armor.chestplateArmorPercent.get() / 100)),
                    Math.round(targetTotal * ((float) Main.CONFIG.armor.leggingsArmorPercent.get() / 100)),
                    Math.round(targetTotal * ((float) Main.CONFIG.armor.helmetArmorPercent.get() / 100)),
                    Math.round(targetTotal * ((float) Main.CONFIG.armor.bootsArmorPercent.get() / 100))
            };
            int body = Math.round(targetTotal * ((float) Main.CONFIG.armor.bodyArmorPercent.get() / 100));
            int total = Arrays.stream(values).sum();
            if (total != targetTotal) {
                int i = 0;
                while (total != targetTotal) {
                    if (total > targetTotal) {
                        values[i]--;
                        total--;
                    } else {
                        values[i]++;
                        total++;
                    }
                    i = i == 3 ? 0 : i + 1;
                }
            }
            map.replaceAll((type, i) -> switch (type) {
                case HELMET -> values[2];
                case CHESTPLATE -> values[0];
                case LEGGINGS -> values[1];
                case BOOTS -> values[3];
                case BODY -> body;
            });
            toughness = 0;
            if (Main.CONFIG.armor.defenseBasedKnockbackResist.get()) {
                knockbackResistance = Mth.lerp(
                        targetTotal / (20 * Main.CONFIG.armor.armorMultiplier.get()), 0, 0.1F
                );
            }
        }
    }
}
