package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.Main;
import me.pajic.rearm.util.ArmorMaterialHelper;
import net.minecraft.resources.ResourceLocation;
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

import java.util.Map;

@Mixin(ArmorMaterial.class)
public class ArmorMaterialMixin {

    @Shadow @Final @Mutable
    private float knockbackResistance;

    @Shadow @Final @Mutable
    private float toughness;

    @Shadow @Final @Mutable
    private Map</*? if 1.21.1 {*/ArmorItem.Type/*?} else {*//*ArmorType*//*?}*/, Integer> defense;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void modifyMaterial(
            CallbackInfo ci,
            @Local(argsOnly = true) /*? if 1.21.1 {*/List<ArmorMaterial.Layer>/*?} else {*//*ResourceKey<EquipmentAsset>*//*?}*/ id
    ) {
        ResourceLocation rl = /*? if 1.21.1 {*/id.isEmpty() ? null : id.getFirst().assetName/*?} else {*//*id.location()*//*?}*/;
        if (rl != null) ArmorMaterialHelper.add(rl);
        if (Main.CONFIG.armor.armorRebalance.get()) {
            int targetTotal = 0;
            if (rl != null && Main.CONFIG.armor.totalArmorOverrides.get().containsKey(rl)) {
                targetTotal = Main.CONFIG.armor.totalArmorOverrides.get().get(rl);
            } else {
                for (Map.Entry</*? if 1.21.1 {*/ArmorItem.Type/*?} else {*//*ArmorType*//*?}*/, Integer> entry : defense.entrySet()) {
                    if (entry.getKey() != /*? if 1.21.1 {*/ArmorItem.Type/*?} else {*//*ArmorType*//*?}*/.BODY)
                        targetTotal += (int) (entry.getValue() * Main.CONFIG.armor.armorMultiplier.get());
                }
            }
            if (targetTotal > 0) defense = ArmorMaterialHelper.calculateDefenses(targetTotal);
            if (Main.CONFIG.armor.enchantmentBasedToughness.get()) toughness = 0;
            if (Main.CONFIG.armor.defenseBasedKnockbackResist.get()) {
                knockbackResistance = ArmorMaterialHelper.calculateKnockbackResist(targetTotal);
            }
        }
    }
}
