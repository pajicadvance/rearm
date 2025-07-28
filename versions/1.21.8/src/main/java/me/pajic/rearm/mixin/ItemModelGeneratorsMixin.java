package me.pajic.rearm.mixin;

import me.pajic.rearm.Main;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.properties.numeric.CrossbowPull;
import net.minecraft.client.renderer.item.properties.numeric.UseDuration;
import net.minecraft.client.renderer.item.properties.select.Charge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(ItemModelGenerators.class)
public abstract class ItemModelGeneratorsMixin {

    @Shadow @Final public ItemModelOutput itemModelOutput;
    @Shadow public abstract ResourceLocation createFlatItemModel(Item item, String suffix, ModelTemplate modelTemplate);

    @Inject(
            method = "run",
            at = @At("TAIL")
    )
    private void registerModels(CallbackInfo ci) {
        ModelTemplate netheriteBowTemplate = new ModelTemplate(Optional.of(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "item/netherite_bow")), Optional.empty(), TextureSlot.LAYER0);
        itemModelOutput
                .accept(
                        ReArmItems.NETHERITE_BOW.value(),
                        ItemModelUtils.conditional(
                                ItemModelUtils.isUsingItem(),
                                ItemModelUtils.rangeSelect(
                                        new UseDuration(false), 0.05F,
                                        ItemModelUtils.plainModel(createFlatItemModel(ReArmItems.NETHERITE_BOW.value(), "_pulling_0", netheriteBowTemplate)),
                                        ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(ReArmItems.NETHERITE_BOW.value(), "_pulling_1", netheriteBowTemplate)), 0.65F),
                                        ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(ReArmItems.NETHERITE_BOW.value(), "_pulling_2", netheriteBowTemplate)), 0.9F)
                                ),
                                ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(ReArmItems.NETHERITE_BOW.value()))
                        )
                );
        ModelTemplate netheriteCrossbowTemplate = new ModelTemplate(Optional.of(ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "item/netherite_crossbow")), Optional.empty(), TextureSlot.LAYER0);
        this.itemModelOutput
                .accept(
                        ReArmItems.NETHERITE_CROSSBOW.value(),
                        ItemModelUtils.select(
                                new Charge(),
                                ItemModelUtils.conditional(
                                        ItemModelUtils.isUsingItem(),
                                        ItemModelUtils.rangeSelect(
                                                new CrossbowPull(), ItemModelUtils.plainModel(this.createFlatItemModel(ReArmItems.NETHERITE_CROSSBOW.value(), "_pulling_0", netheriteCrossbowTemplate)),
                                                ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(ReArmItems.NETHERITE_CROSSBOW.value(), "_pulling_1", netheriteCrossbowTemplate)), 0.58F),
                                                ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(ReArmItems.NETHERITE_CROSSBOW.value(), "_pulling_2", netheriteCrossbowTemplate)), 1.0F)
                                        ),
                                        ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(ReArmItems.NETHERITE_CROSSBOW.value()))
                                ),
                                ItemModelUtils.when(CrossbowItem.ChargeType.ARROW, ItemModelUtils.plainModel(this.createFlatItemModel(ReArmItems.NETHERITE_CROSSBOW.value(), "_arrow", netheriteCrossbowTemplate))),
                                ItemModelUtils.when(CrossbowItem.ChargeType.ROCKET, ItemModelUtils.plainModel(this.createFlatItemModel(ReArmItems.NETHERITE_CROSSBOW.value(), "_firework", netheriteCrossbowTemplate)))
                        )
                );
    }
}
