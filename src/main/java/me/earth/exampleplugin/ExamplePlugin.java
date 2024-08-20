package me.earth.exampleplugin;

import me.earth.earthhack.api.plugin.Plugin;
import me.earth.earthhack.api.register.exception.AlreadyRegisteredException;
import me.earth.earthhack.impl.managers.Managers;
import me.earth.exampleplugin.module.ExampleModule;

// The main class of the plugin
@SuppressWarnings("unused")
public class ExamplePlugin implements Plugin {

    // This is always at the start of the game
    @Override
    public void load() {
        System.out.println("Hello from the ExamplePlugin!");
    }

    // This could be loaded at any time
    @Override
    public void loadRuntime() {
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
