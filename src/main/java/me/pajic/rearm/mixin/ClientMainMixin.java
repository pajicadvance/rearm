package me.pajic.rearm.mixin;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import me.pajic.rearm.util.ArmorMaterialHelper;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(Minecraft.class)
public class ClientMainMixin {

    @Inject(
            method = "onGameLoadFinished",
            at = @At("TAIL")
    )
    private void onGameLoadFinished(CallbackInfo ci) {
        ArmorMaterialHelper.write();
    }
}
