package com.mattzm.gungale.client.settings;

import com.mattzm.gungale.util.math.ModMathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class ModSettings {
    private static final KeyBinding KEY_FIRE = new KeyBinding("key.fire", ModKeyConflictContext.ON_SHOOTING, Type.MOUSE, 0, "key.categories.gungale");
    private static final KeyBinding KEY_RELOAD = new KeyBinding("key.reload", ModKeyConflictContext.ON_SHOOTING, Type.KEYSYM, 82, "key.categories.gungale");
    private static final KeyBinding[] MAP = new KeyBinding[]{KEY_FIRE, KEY_RELOAD};

    public static KeyBinding getKey(int index) {
        return getMap()[index];
    }

    public static KeyBinding getModKey(int index) {
        int realIndex = getMap().length - MAP.length + index;
        return getMap()[realIndex];
    }

    public static double getVerticalFOV() {
        return Minecraft.getInstance().options.fov;
    }

    public static double getHorizontalFOV() {
        double scale = 16.0 / 9.0 * Math.tan(ModMathHelper.getRadius(getVerticalFOV() / 2));
        return ModMathHelper.getDegree(2 * Math.atan(scale));
    }

    public static void setupAll() {
        addAllConflictContext();
        registerKeyBindings();
        registerSettings();
    }

    private static KeyBinding[] getMap() {
        return Minecraft.getInstance().options.keyMappings;
    }

    private static void addAllConflictContext() {
        for (KeyBinding keyBinding : getMap()) keyBinding.setKeyConflictContext(KeyConflictContext.UNIVERSAL);
        getKey(0).setKeyConflictContext(ModKeyConflictContext.OFF_SHOOTING);
    }

    private static void registerKeyBindings() {
        for (KeyBinding keyBinding : MAP) ClientRegistry.registerKeyBinding(keyBinding);
    }

    private static void registerSettings() {
        Minecraft.getInstance().options.renderDistance = 22;
    }
}
