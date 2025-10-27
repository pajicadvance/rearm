package me.pajic.rearm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.pajic.rearm.renderer.RendererConstants;
import net.minecraft.client.data.AtlasProvider;
import net.minecraft.client.renderer.MaterialMapper;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.CachedOutput;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mixin(AtlasProvider.class)
public abstract class AtlasProviderMixin {

    @Shadow
    protected abstract CompletableFuture<?> storeAtlas(CachedOutput output, ResourceLocation atlasId, List<SpriteSource> sources);

    @WrapOperation(
            method = "run",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/concurrent/CompletableFuture;allOf([Ljava/util/concurrent/CompletableFuture;)Ljava/util/concurrent/CompletableFuture;"
            )
    )
    private CompletableFuture<Void> addModAtlas(CompletableFuture<?>[] cfs, Operation<CompletableFuture<Void>> original, @Local(argsOnly = true) CachedOutput output) {
        MaterialMapper NETHERITE_SHIELD_MAPPER = new MaterialMapper(
                RendererConstants.NETHERITE_SHIELD_PATTERNS, "entity/shield"
        );
        List<CompletableFuture<?>> futures = Arrays.asList(cfs);
        futures.add(storeAtlas(output, RendererConstants.NETHERITE_SHIELD_PATTERNS, List.of(
                new SingleFile(RendererConstants.NETHERITE_SHIELD_BASE.texture()),
                new SingleFile(RendererConstants.NO_PATTERN_NETHERITE_SHIELD.texture()),
                new DirectoryLister(
                        NETHERITE_SHIELD_MAPPER.prefix(),
                        NETHERITE_SHIELD_MAPPER.prefix() + "/"
                )
        )));
        return original.call((Object) futures.toArray(new CompletableFuture[0]));
    }
}
