package me.pajic.rearm.mixin.modern;

//? if > 1.21.1 {

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.ReArm;
import me.pajic.rearm.item.ReArmItems;
import me.pajic.rearm.renderer.NetheriteShieldSpecialRenderer;
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

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(ItemModelGenerators.class)
public abstract class ItemModelGeneratorsMixin {

    @Shadow @Final public ItemModelOutput itemModelOutput;
    @Shadow public abstract ResourceLocation createFlatItemModel(Item item, String suffix, ModelTemplate modelTemplate);

    @Inject(
            method = "run",
            at = @At("TAIL")
    )
    private void registerModels(CallbackInfo ci) {
        ModelTemplate netheriteBowTemplate = new ModelTemplate(Optional.of(ReArm.id("item/netherite_bow")), Optional.empty(), TextureSlot.LAYER0);
        itemModelOutput
                .accept(
                        ReArmItems.NETHERITE_BOW,
                        ItemModelUtils.conditional(
                                ItemModelUtils.isUsingItem(),
                                ItemModelUtils.rangeSelect(
                                        new UseDuration(false), 0.05F,
                                        ItemModelUtils.plainModel(createFlatItemModel(ReArmItems.NETHERITE_BOW, "_pulling_0", netheriteBowTemplate)),
                                        ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(ReArmItems.NETHERITE_BOW, "_pulling_1", netheriteBowTemplate)), 0.65F),
                                        ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(ReArmItems.NETHERITE_BOW, "_pulling_2", netheriteBowTemplate)), 0.9F)
                                ),
                                ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(ReArmItems.NETHERITE_BOW))
                        )
                );
        ModelTemplate netheriteCrossbowTemplate = new ModelTemplate(Optional.of(ReArm.id("item/netherite_crossbow")), Optional.empty(), TextureSlot.LAYER0);
        this.itemModelOutput
                .accept(
                        ReArmItems.NETHERITE_CROSSBOW,
                        ItemModelUtils.select(
                                new Charge(),
                                ItemModelUtils.conditional(
                                        ItemModelUtils.isUsingItem(),
                                        ItemModelUtils.rangeSelect(
                                                new CrossbowPull(), ItemModelUtils.plainModel(this.createFlatItemModel(ReArmItems.NETHERITE_CROSSBOW, "_pulling_0", netheriteCrossbowTemplate)),
                                                ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(ReArmItems.NETHERITE_CROSSBOW, "_pulling_1", netheriteCrossbowTemplate)), 0.58F),
                                                ItemModelUtils.override(ItemModelUtils.plainModel(this.createFlatItemModel(ReArmItems.NETHERITE_CROSSBOW, "_pulling_2", netheriteCrossbowTemplate)), 1.0F)
                                        ),
                                        ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(ReArmItems.NETHERITE_CROSSBOW))
                                ),
                                ItemModelUtils.when(CrossbowItem.ChargeType.ARROW, ItemModelUtils.plainModel(this.createFlatItemModel(ReArmItems.NETHERITE_CROSSBOW, "_arrow", netheriteCrossbowTemplate))),
                                ItemModelUtils.when(CrossbowItem.ChargeType.ROCKET, ItemModelUtils.plainModel(this.createFlatItemModel(ReArmItems.NETHERITE_CROSSBOW, "_firework", netheriteCrossbowTemplate)))
                        )
                );
        this.itemModelOutput
                .accept(
                        ReArmItems.NETHERITE_SHIELD,
                        ItemModelUtils.conditional(
                                ItemModelUtils.isUsingItem(),
                                ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(ReArmItems.NETHERITE_SHIELD), new NetheriteShieldSpecialRenderer.Unbaked()),
                                ItemModelUtils.specialModel(ModelLocationUtils.getModelLocation(ReArmItems.NETHERITE_SHIELD, "_blocking"), new NetheriteShieldSpecialRenderer.Unbaked())
                        )
                );
    }
}
//?}
