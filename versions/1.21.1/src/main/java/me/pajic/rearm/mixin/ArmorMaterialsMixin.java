package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import me.pajic.rearm.Main;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

@Mixin(ArmorMaterials.class)
public class ArmorMaterialsMixin {

    @Inject(
            method = "register(Ljava/lang/String;Ljava/util/EnumMap;ILnet/minecraft/core/Holder;FFLjava/util/function/Supplier;Ljava/util/List;)Lnet/minecraft/core/Holder;",
            at = @At("HEAD")
    )
    private static void modifyMaterial(
            CallbackInfoReturnable<Holder<ArmorMaterial>> cir,
            @Local(argsOnly = true) String name,
            @Local(argsOnly = true) EnumMap<ArmorItem.Type, Integer> map,
            @Local(argsOnly = true, ordinal = 0) LocalFloatRef toughness,
            @Local(argsOnly = true, ordinal = 1) LocalFloatRef knockbackResistance
    ) {
        if (Main.CONFIG.armor.armorRebalance.get()) {
            int targetTotal = 0;
            if (Main.CONFIG.armor.totalArmorOverrides.get().containsKey(name)) {
                targetTotal = Main.CONFIG.armor.totalArmorOverrides.get().get(name);
            } else for (Map.Entry<ArmorItem.Type, Integer> entry : map.entrySet()) {
                if (entry.getKey() != ArmorItem.Type.BODY) targetTotal += (int) (entry.getValue() * Main.CONFIG.armor.armorMultiplier.get());
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
            toughness.set(0);
            if (Main.CONFIG.armor.defenseBasedKnockbackResist.get()) {
                knockbackResistance.set(Mth.lerp(
                        targetTotal / (20 * Main.CONFIG.armor.armorMultiplier.get()), 0, 0.1F
                ));
            }
        }
    }
}
