package me.pajic.rearm.mixin.compat.ia;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import immersive_armors.Items;
import immersive_armors.item.ExtendedArmorMaterial;
import me.pajic.rearm.Main;
import me.pajic.rearm.util.ArmorMaterialHelper;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
//? if 1.21.1
import net.minecraft.world.item.ArmorItem;

import java.util.Map;

@IfModLoaded("immersive_armors")
@Mixin(Items.class)
public interface ItemsMixin {

    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Limmersive_armors/item/ExtendedArmorMaterial;equipSound(Lnet/minecraft/sounds/SoundEvent;)Limmersive_armors/item/ExtendedArmorMaterial;"
            )
    )
    private static ExtendedArmorMaterial modifyMaterial1(ExtendedArmorMaterial original) {
        return modifyMaterial(original);
    }

    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Limmersive_armors/item/ExtendedArmorMaterial;equipSound(Lnet/minecraft/core/Holder;)Limmersive_armors/item/ExtendedArmorMaterial;"
            )
    )
    private static ExtendedArmorMaterial modifyMaterial2(ExtendedArmorMaterial original) {
        return modifyMaterial(original);
    }

    @Unique
    private static ExtendedArmorMaterial modifyMaterial(ExtendedArmorMaterial original) {
        //? if 1.21.1 {
        ResourceLocation rl = ResourceLocation.fromNamespaceAndPath("immersive_armors", original.getName());
        ArmorMaterialHelper.add(rl);
        if (Main.CONFIG.armor.armorRebalance.get()) {
            int targetTotal = 0;
            if (Main.CONFIG.armor.totalArmorOverrides.get().containsKey(rl)) {
                targetTotal = Main.CONFIG.armor.totalArmorOverrides.get().get(rl);
            } else {
                for (Map.Entry</*? if 1.21.1 {*/ArmorItem.Type/*?} else {*//*ArmorType*//*?}*/, Integer> entry : original.getProtection().entrySet()) {
                    if (entry.getKey() != /*? if 1.21.1 {*/ArmorItem.Type/*?} else {*//*ArmorType*//*?}*/.BODY)
                        targetTotal += (int) (entry.getValue() * Main.CONFIG.armor.armorMultiplier.get());
                }
            }
            if (targetTotal > 0) {
                Map</*? if 1.21.1 {*/ArmorItem.Type/*?} else {*//*ArmorType*//*?}*/, Integer> map = ArmorMaterialHelper.calculateDefenses(targetTotal);
                original.protectionAmount(
                        map.get(/*? if 1.21.1 {*/ArmorItem.Type/*?} else {*//*ArmorType*//*?}*/.HELMET),
                        map.get(/*? if 1.21.1 {*/ArmorItem.Type/*?} else {*//*ArmorType*//*?}*/.CHESTPLATE),
                        map.get(/*? if 1.21.1 {*/ArmorItem.Type/*?} else {*//*ArmorType*//*?}*/.LEGGINGS),
                        map.get(/*? if 1.21.1 {*/ArmorItem.Type/*?} else {*//*ArmorType*//*?}*/.BOOTS)
                );
            }
            if (Main.CONFIG.armor.enchantmentBasedToughness.get()) original.toughness(0);
            if (Main.CONFIG.armor.defenseBasedKnockbackResist.get()) {
                original.knockbackReduction(ArmorMaterialHelper.calculateKnockbackResist(targetTotal));
            }
        }
        //?}
        return original;
    }
}
