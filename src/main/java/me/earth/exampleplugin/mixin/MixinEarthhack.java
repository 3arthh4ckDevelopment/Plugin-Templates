package me.earth.exampleplugin.mixin;

import me.earth.earthhack.impl.Earthhack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Earthhack.class, remap = false)
public class MixinEarthhack {

    // Mixin to the onInitializeClient method of the Earthhack class
    @Inject(method = "onInitializeClient", at = @At("HEAD"))
    private void initHook(CallbackInfo info)
    {
        System.out.println("Mixin in the Earthhack class!");
    }
}
