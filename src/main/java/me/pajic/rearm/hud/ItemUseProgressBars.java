package me.pajic.rearm.hud;

import me.pajic.rearm.ReArm;
import me.pajic.rearm.item.ReArmItems;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class ItemUseProgressBars {

	public static final Set<ItemUseProgressBar> bars = Arrays.stream(ItemUseProgressBarType.values())
			.map(ItemUseProgressBar::new)
			.collect(Collectors.toUnmodifiableSet());

	private static final Identifier BACKGROUND_TIMED = Identifier.withDefaultNamespace("hud/jump_bar_background");
	private static final Identifier PROGRESS_TIMED = Identifier.withDefaultNamespace("hud/jump_bar_progress");
	private static final Identifier BACKGROUND = ReArm.id("hud/item_progress_bar_background");
	private static final Identifier PROGRESS = ReArm.id("hud/item_progress_bar_progress");
	private static final Identifier CHARGED = ReArm.id("hud/item_progress_bar_charged");

	public static class ItemUseProgressBar implements ContextualBarRenderer {
		private final Minecraft minecraft;
		private final ItemUseProgressBarType type;
		private final Identifier id;

		private ItemUseProgressBar(ItemUseProgressBarType type) {
			minecraft = Minecraft.getInstance();
			this.type = type;
			id = ReArm.id("item_progress_bar_" + type.name().toLowerCase(Locale.ROOT));
		}

		@Override
		public void extractBackground(@NotNull final GuiGraphicsExtractor graphics, @NotNull final DeltaTracker deltaTracker) {
			if (minecraft.player != null && type.renderCondition.test(minecraft.player.getUseItem())) {
				int left = left(minecraft.getWindow());
				int top = top(minecraft.getWindow());
				int useTime = minecraft.player.getTicksUsingItem();
				int maxUseTime = type.duration.applyAsInt(minecraft.player);
				graphics.blitSprite(RenderPipelines.GUI_TEXTURED, type.isTimed ? BACKGROUND_TIMED : BACKGROUND, left, top, 182, 5);
				int progress = Math.min(Mth.lerpDiscrete((float) useTime / maxUseTime, 0, 182), 182);
				int mod = (useTime - maxUseTime) % 10;
				if (progress == 182 && mod < 5) {
					graphics.blitSprite(RenderPipelines.GUI_TEXTURED, CHARGED, left, top, 182, 5);
				} else if (progress > 0) {
					graphics.blitSprite(RenderPipelines.GUI_TEXTURED, type.isTimed ? PROGRESS_TIMED : PROGRESS, 182, 5, 0, 0, left, top, progress, 5);
				}
			}
		}

		@Override
		public void extractRenderState(@NotNull final GuiGraphicsExtractor graphics, @NotNull final DeltaTracker deltaTracker) {
		}

		public Identifier getId() {
			return id;
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof ItemUseProgressBar bar && bar.id.equals(id);
		}
	}

	private enum ItemUseProgressBarType {
		BOW(true, stack -> ReArm.CONFIG.bow.chargeBar.get() && ReArmItems.isBow(stack), _ -> ReArm.CONFIG.bow.enablePerfectShot.get() ? 20 + ReArm.CONFIG.bow.perfectShotTimeframe.get() : 20),
		CROSSBOW(false, stack -> ReArm.CONFIG.crossbow.chargeBar.get() && ReArmItems.isCrossbow(stack), p -> CrossbowItem.getChargeDuration(p.getUseItem(), p)),
		AXE(false, stack -> ReArm.CONFIG.axe.chargeBar.get() && stack.is(ItemTags.AXES), _ -> 10),
		TRIDENT(false, stack -> ReArm.CONFIG.trident.chargeBar.get() && stack.is(Items.TRIDENT), _ -> 10);

		private final boolean isTimed;
		private final Predicate<ItemStack> renderCondition;
		private final ToIntFunction<Player> duration;

		ItemUseProgressBarType(boolean isTimed, Predicate<ItemStack> renderCondition, ToIntFunction<Player> duration) {
			this.isTimed = isTimed;
			this.renderCondition = renderCondition;
			this.duration = duration;
		}
	}
}
