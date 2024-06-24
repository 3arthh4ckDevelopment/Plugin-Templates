package me.earth.exampleplugin.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    // Mixin to the constructor of the MinecraftClient class
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void constructorHook(CallbackInfo callbackInfo)
    {
        System.out.println("Mixin in the MinecraftClient class!");
    }
}
