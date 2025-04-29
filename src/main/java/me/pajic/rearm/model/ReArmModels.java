package me.pajic.rearm.model;

import me.pajic.rearm.item.NetheriteCrossbowItem;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;

public class ReArmModels {

    public static void initModels() {
        registerNetheriteBow();
        registerNetheriteCrossbow();
    }

    private static void registerNetheriteBow() {
        ItemProperties.register(
                ReArmItems.NETHERITE_BOW.value(), ResourceLocation.withDefaultNamespace("pull"),
                (itemStack, clientLevel, livingEntity, i) -> {
                    if (livingEntity == null) {
                        return 0.0F;
                    } else {
                        return livingEntity.getUseItem() != itemStack ?
                                0.0F
                                : (float)(itemStack.getUseDuration(livingEntity)
                                - livingEntity.getUseItemRemainingTicks()) / 20.0F;
                    }
                }
        );
        ItemProperties.register(
                ReArmItems.NETHERITE_BOW.value(),
                ResourceLocation.withDefaultNamespace("pulling"),
                (itemStack, clientLevel, livingEntity, i) ->
                        livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
        );
    }

    private static void registerNetheriteCrossbow() {
        ItemProperties.register(
                ReArmItems.NETHERITE_CROSSBOW.value(),
                ResourceLocation.withDefaultNamespace("pull"),
                (itemStack, clientLevel, livingEntity, i) -> {
                    if (livingEntity == null) {
                        return 0.0F;
                    } else {
                        return NetheriteCrossbowItem.isCharged(itemStack)
                                ? 0.0F
                                : (float)(itemStack.getUseDuration(livingEntity) - livingEntity.getUseItemRemainingTicks())
                                / (float)NetheriteCrossbowItem.getChargeDuration(itemStack, livingEntity);
                    }
                }
        );
        ItemProperties.register(
                ReArmItems.NETHERITE_CROSSBOW.value(),
                ResourceLocation.withDefaultNamespace("pulling"),
                (itemStack, clientLevel, livingEntity, i) -> livingEntity != null
                        && livingEntity.isUsingItem()
                        && livingEntity.getUseItem() == itemStack
                        && !NetheriteCrossbowItem.isCharged(itemStack)
                        ? 1.0F
                        : 0.0F
        );
        ItemProperties.register(
                ReArmItems.NETHERITE_CROSSBOW.value(),
                ResourceLocation.withDefaultNamespace("charged"),
                (itemStack, clientLevel, livingEntity, i) ->
                        NetheriteCrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F
        );
        ItemProperties.register(
                ReArmItems.NETHERITE_CROSSBOW.value(), ResourceLocation.withDefaultNamespace("firework"),
                (itemStack, clientLevel, livingEntity, i) -> {
                    ChargedProjectiles chargedProjectiles = itemStack.get(DataComponents.CHARGED_PROJECTILES);
                    return chargedProjectiles != null && chargedProjectiles.contains(Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
                }
        );
    }
}
