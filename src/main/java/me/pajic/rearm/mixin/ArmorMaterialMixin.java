package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.util.ArmorMaterialHelper;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

//? <26.1 {
/*import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import java.util.List;
*///?} else {
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
//?}

@IfModAbsent("apothic_attributes")
@Mixin(ArmorMaterial.class)
public class ArmorMaterialMixin {

    //~ if <26.1 'ArmorType' -> 'ArmorItem.Type' {
    @Shadow @Final @Mutable private float knockbackResistance;
	@Shadow @Final @Mutable private float toughness;
	@Shadow @Final @Mutable private Map<ArmorType, Integer> defense;

    @SuppressWarnings("ConstantValue")
	@Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void modifyMaterial(
            CallbackInfo ci,
            //~ if <26.1 'ResourceKey<EquipmentAsset>' -> 'List<ArmorMaterial.Layer>'
            @Local(argsOnly = true) ResourceKey<EquipmentAsset> assetId
    ) {
        //~ if <26.1 'assetId.identifier()' -> 'assetId.isEmpty() ? null : assetId.getFirst().assetName'
        Identifier rl = assetId.identifier();
        if (rl != null) ArmorMaterialHelper.add(rl);
        if (ReArm.CONFIG.armor.armorRebalance.get()) {
            int targetTotal = 0;
            if (rl != null && ReArm.CONFIG.armor.totalArmorOverrides.get().containsKey(rl)) {
                targetTotal = ReArm.CONFIG.armor.totalArmorOverrides.get().get(rl);
            } else {
                for (Map.Entry<ArmorType, Integer> entry : defense.entrySet()) {
                    if (entry.getKey() != ArmorType.BODY) {
						targetTotal += (int) (entry.getValue() * ReArm.CONFIG.armor.armorMultiplier.get());
					}
                }
            }
            if (targetTotal > 0) defense = ArmorMaterialHelper.calculateDefenses(targetTotal);
            if (ReArm.CONFIG.armor.enchantmentBasedToughness.get()) toughness = 0;
            if (ReArm.CONFIG.armor.defenseBasedKnockbackResist.get()) {
                knockbackResistance = ArmorMaterialHelper.calculateKnockbackResist(targetTotal);
            }
        }
    }
    //~}
}
