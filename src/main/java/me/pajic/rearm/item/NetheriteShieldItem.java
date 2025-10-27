package me.pajic.rearm.item;

import me.pajic.rearm.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.jetbrains.annotations.NotNull;
//? if 1.21.1 {
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
//?} else {
/*import net.minecraft.world.item.component.BlocksAttacks;
 *///?}

import java.util.List;
import java.util.Optional;

public class NetheriteShieldItem extends ShieldItem {
    public NetheriteShieldItem() {
        super(
                new Properties().fireResistant().stacksTo(1).durability(614)
                .component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
                //? if > 1.21.1 {
                /*.repairable(Items.NETHERITE_INGOT).enchantable(14)
                .equippableUnswappable(EquipmentSlot.OFFHAND)
                .component(
                        DataComponents.BLOCKS_ATTACKS,
                        new BlocksAttacks(
                                0.25F,
                                1.0F,
                                List.of(new BlocksAttacks.DamageReduction(90.0F, Optional.empty(), 0.0F, 1.0F)),
                                new BlocksAttacks.ItemDamageFunction(3.0F, 1.0F, 1.0F),
                                Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                                Optional.of(SoundEvents.SHIELD_BLOCK),
                                Optional.of(SoundEvents.SHIELD_BREAK)
                        )
                )
                .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
                .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "netherite_shield")))
                *///?}
        );
    }

    //? if 1.21.1 {
    @Override
    public boolean isValidRepairItem(@NotNull ItemStack stack, @NotNull ItemStack repairCandidate) {
        return Ingredient.of(Items.NETHERITE_INGOT).test(repairCandidate) || super.isValidRepairItem(stack, repairCandidate);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getEnchantmentValue() {
        return 14;
    }
    //?}

    @Override
    public boolean isEnabled(@NotNull FeatureFlagSet enabledFeatures) {
        return Main.CONFIG.shield.shieldNetheriteVariant.get();
    }
}
