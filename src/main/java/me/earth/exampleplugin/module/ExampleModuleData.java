package me.earth.exampleplugin.module;

import me.earth.earthhack.api.module.data.DefaultData;

public class ExampleModuleData extends DefaultData<ExampleModule> {

    public ExampleModuleData(ExampleModule module) {
        super(module);
        register(module.chatMessage, "If the message should be printed in the chat");
        register(module.text, "The text that will be printed in the chat");
        register(module.number, "The distance from the player to the block");
        register(module.color, "The color of the box that will be drawn");
    }
}
