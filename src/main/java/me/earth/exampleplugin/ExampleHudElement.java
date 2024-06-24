package me.earth.exampleplugin;

import me.earth.earthhack.api.hud.HudCategory;
import me.earth.earthhack.api.hud.HudElement;
import me.earth.earthhack.api.setting.Setting;
import me.earth.earthhack.api.setting.settings.BooleanSetting;
import me.earth.earthhack.api.setting.settings.ColorSetting;
import me.earth.earthhack.api.setting.settings.EnumSetting;
import me.earth.earthhack.api.setting.settings.NumberSetting;
import me.earth.earthhack.impl.util.render.Render2DUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.awt.*;

public class ExampleHudElement extends HudElement {

    // A boolean, number, color and enum setting
    private final Setting<Boolean> item =
            register(new BooleanSetting("Item-Crystal", false));
    private final Setting<Integer> number =
            register(new NumberSetting<>("Items-Count", 25, 0, 100));
    private final Setting<Color> color =
            register(new ColorSetting("Color", new Color(64, 233, 20, 255)));
    private final Setting<ExampleEnumMode> mode =
            register(new EnumSetting<>("Mode", ExampleEnumMode.Center));

    // This method is called every frame
    @Override
    protected void onRender(DrawContext drawContext) {
        // Draw a rectangle on the screen
        Render2DUtil.drawRect(drawContext.getMatrices(), getX(), getY(), getX() + getWidth(), getY() + getHeight(), color.getValue().getRGB());

        // Create an ItemStack based on the boolean setting
        ItemStack itemStack;
        if (item.getValue()) {
            itemStack = new ItemStack(Items.END_CRYSTAL, number.getValue());
        } else {
            itemStack = new ItemStack(Items.TOTEM_OF_UNDYING, number.getValue());
        }

        // Draw the item on the screen based on the enum setting
        float y = switch (mode.getValue()) {
            case Top -> getY();
            case Center -> getY() + getHeight() / 2 - 8;
            case Bottom -> getY() + getHeight() - 16;
        };

        // Calculate the x position based on the width of the element, so it's always centered
        float x = (getX() + (getWidth() / 2 - 8));

        // Draw the item on the screen
        Render2DUtil.drawItem(drawContext, itemStack, (int) x, (int) y, true);
    }

    // Constructor of the HudElement
    public ExampleHudElement() {
        super("ExampleHudElement", "An example Hud Element", HudCategory.Visual, 100, 100);
    }

    // Set the width and height of the element
    @Override
    public float getWidth() {
        return 100.0f;
    }

    @Override
    public float getHeight() {
        return 100.0f;
    }

    // The enum for the mode setting
    private enum ExampleEnumMode {
        Top, Center, Bottom
    }
}
