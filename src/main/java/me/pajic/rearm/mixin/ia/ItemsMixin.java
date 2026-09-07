package me.pajic.rearm.mixin.ia;

import com.moulberry.mixinconstraints.annotations.IfModAbsent;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import immersive_armors.Items;
import immersive_armors.item.ExtendedArmorMaterial;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.util.ArmorMaterialHelper;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.Map;

//~ if <26.1 'equipment.ArmorType' -> 'ArmorItem'
import net.minecraft.world.item.equipment.ArmorType;

@IfModAbsent("apothic_attributes")
@IfModLoaded("immersive_armors")
@Mixin(Items.class)
public interface ItemsMixin {

	@SuppressWarnings("ConstantValue")
	@Inject(
			method = {"registerSet", "registerDyeableSet"},
			at = @At("HEAD")
	)
	private static void modifyMaterial(
			ExtendedArmorMaterial material,
			CallbackInfoReturnable<ExtendedArmorMaterial> cir
	) {
        //~ if <26.1 'ArmorType' -> 'ArmorItem.Type' {
        //~ if <26.1 'material.getMaterial().assetId().identifier()' -> 'ResourceLocation.fromNamespaceAndPath("immersive_armors", material.getName())'
		Identifier rl = material.getMaterial().assetId().identifier();
		if (rl != null) ArmorMaterialHelper.add(rl);
		if (ReArm.CONFIG.armor.armorRebalance.get()) {
			int targetTotal = 0;
			if (rl != null && ReArm.CONFIG.armor.totalArmorOverrides.get().containsKey(rl)) {
				targetTotal = ReArm.CONFIG.armor.totalArmorOverrides.get().get(rl);
			} else {
				for (Map.Entry<ArmorType, Integer> entry : material.getProtection().entrySet()) {
					if (entry.getKey() != ArmorType.BODY) {
						targetTotal += (int) (entry.getValue() * ReArm.CONFIG.armor.armorMultiplier.get());
					}
				}
			}
			if (targetTotal > 0) {
				Map<ArmorType, Integer> map = ArmorMaterialHelper.calculateDefenses(targetTotal);
				material.protectionAmount(
						map.get(ArmorType.HELMET),
						map.get(ArmorType.CHESTPLATE),
						map.get(ArmorType.LEGGINGS),
						map.get(ArmorType.BOOTS)
				);
			}
			if (ReArm.CONFIG.armor.enchantmentBasedToughness.get()) material.toughness(0);
			if (ReArm.CONFIG.armor.defenseBasedKnockbackResist.get()) {
				material.knockbackReduction(ArmorMaterialHelper.calculateKnockbackResist(targetTotal));
			}
		}
        //~}
	}
}
