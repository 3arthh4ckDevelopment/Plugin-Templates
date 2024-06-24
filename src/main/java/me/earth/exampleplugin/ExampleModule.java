package me.earth.exampleplugin;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.event.events.misc.TickEvent;
import me.earth.earthhack.impl.event.listeners.LambdaListener;
import me.earth.earthhack.impl.util.text.ChatUtil;

public class ExampleModule extends Module {

    // A String setting
    private final Setting<String> text =
            register(new StringSetting("Text", "Example text!"));

    public ExampleModule() {
        super("ExampleModule", Category.Client);

        // A simple listener that prints "hello!!" to the chat every tick
        this.listeners.add(new LambdaListener<>(TickEvent.class, e -> {
            // Check if the player and world are not null
            if (mc.player != null && mc.world != null) {
                // Print the message in the minecraft chat
                ChatUtil.sendMessage(text.getValue()); // Get the value of the text setting
            }
        }));
    }

    @Override
    public void onEnable() {
        // Print a message to the console when the module is enabled
        System.out.println("ExamplePlugins Module enabled!");
    }

    @Override
    public void onDisable() {
        // Print a message to the console when the module is disabled
        System.out.println("ExamplePlugins Module disabled!");
    }
}
