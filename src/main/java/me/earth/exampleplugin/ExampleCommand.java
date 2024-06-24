package me.earth.exampleplugin;

import me.earth.earthhack.api.command.Command;
import me.earth.earthhack.impl.util.text.ChatUtil;

public class ExampleCommand extends Command {

    // Constructor of the Command
    public ExampleCommand() {
        super(new String[][]{{"ExampleCommand"}});
    }

    // This method is called when the command is executed
    @Override
    public void execute(String[] args) {
        ChatUtil.sendMessage("Example command executed!");
    }
}
