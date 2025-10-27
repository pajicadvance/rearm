package me.pajic.rearm.projectile;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ThrownAxeRenderState extends EntityRenderState {
    public final ItemStackRenderState item = new ItemStackRenderState();
    public float xRot;
    public float yRot;
    public boolean stuck;
    public ItemStack axe;
    public float partialTick;

    public void extractItemGroupRenderState(Entity entity, ItemModelResolver itemModelResolver) {
        itemModelResolver.updateForNonLiving(item, axe, ItemDisplayContext.FIXED, entity);
    }
}
