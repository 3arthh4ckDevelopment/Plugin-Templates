package me.earth.exampleplugin.module;

import me.earth.earthhack.api.module.Module;
import me.earth.earthhack.api.module.util.Category;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.api.setting.settings.StringSetting;
import me.earth.earthhack.impl.event.events.misc.TickEvent;
import me.earth.earthhack.impl.event.listeners.LambdaListener;
import me.earth.earthhack.impl.util.text.ChatUtil;

import java.awt.*;

public class ExampleModule extends Module {

    // A boolean, String, number and color setting
    protected final Setting<Boolean> chatMessage =
            register(new BooleanSetting("ChatMessage", true));
    protected final Setting<String> text =
            register(new StringSetting("Text", "Example text!"));
    protected final Setting<Integer> number =
            register(new NumberSetting<>("Distance", 5, 0, 10));
    protected final Setting<Color> color =
            register(new ColorSetting("Color", new Color(30, 136, 229, 255)));

    // Constructor of the module
    public ExampleModule() {
        super("ExampleModule", Category.Client);

        // A simple listener that prints "hello!!" to the chat every tick
        this.listeners.add(new LambdaListener<>(TickEvent.class, e -> {
            // Check if the player and world are not null
            if (mc.player != null && mc.world != null) {
                // Check if the chat setting is enabled
                if (chatMessage.getValue()) {
                    // Print the message in the minecraft chat
                    ChatUtil.sendMessage(text.getValue()); // Get the value of the text setting
                }
            }
        }));

        // Add a module listener that draws a box under the player's feet
        this.listeners.add(new ListenerRender(this));

        // Set the data of the module
        this.setData(new ExampleModuleData(this));
    }

    @Override
    public void onEnable() {
        // Print a message to the console when the module is enabled
        ChatUtil.sendMessage("ExamplePlugins Module enabled!");
    }

    @Override
    public void onDisable() {
        // Print a message to the console when the module is disabled
        ChatUtil.sendMessage("ExamplePlugins Module disabled!");
    }
}
