package me.earth.exampleplugin;

import me.earth.earthhack.api.plugin.Plugin;
import me.earth.earthhack.api.register.exception.AlreadyRegisteredException;
import me.earth.earthhack.impl.managers.Managers;
import net.minecraft.client.MinecraftClient;

// The main class of the plugin
@SuppressWarnings("unused")
public class PluginExample implements Plugin {

    @Override
    public void load() {
        System.out.println("Hello from the ExamplePlugin!");

        // Accessing a private field from the Minecraft class using the AccessWidener
        System.out.println("Start time: " + MinecraftClient.getInstance().startTime);

        try {
            // Registering a module
            Managers.MODULES.register(new ExampleModule());

            // Registering a command
            Managers.COMMANDS.register(new ExampleCommand());

            // Registering a hud element
            Managers.ELEMENTS.register(new ExampleHudElement());
        } catch (AlreadyRegisteredException e) {
            throw new RuntimeException(e);
        }
    }
}
