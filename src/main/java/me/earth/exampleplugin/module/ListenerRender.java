package me.earth.exampleplugin.module;

import me.earth.earthhack.impl.event.events.render.Render3DEvent;
import me.earth.earthhack.impl.event.listeners.ModuleListener;
import me.earth.earthhack.impl.util.render.Interpolation;
import me.earth.earthhack.impl.util.render.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class ListenerRender extends ModuleListener<ExampleModule, Render3DEvent> {

    // Constructor that takes the module as an argument
    public ListenerRender(ExampleModule module) {
        super(module, Render3DEvent.class);
    }

    // This method is called when the event is fired
    @Override
    public void invoke(Render3DEvent event) {
        // Check if the world is not null
        if (mc.world == null) {
            return;
        }

        // Get the position of the player and move it down by the number of blocks
        BlockPos pos = new BlockPos(mc.player.getBlockPos().down(module.number.getValue()));

        // Interpolate the position to get a box
        Box bb = Interpolation.interpolatePos(pos, 1);

        // Draw the outline of the box
        RenderUtil.drawOutline(event.getStack(), bb, 2, module.color.getValue());
    }
}
