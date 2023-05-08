package com.mattzm.gungale.client.object;

import com.mattzm.gungale.client.gui.ModIngameGui;
import com.mattzm.gungale.client.renderer.ModFirstPersonRenderer;
import com.mattzm.gungale.client.renderer.entity.ModPlayerRenderer;
import com.mattzm.gungale.client.settings.ModGameSettings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ClientObjectHolder {
    private static ClientObjectHolder instance;
    private final Minecraft minecraft;
    private ModGameSettings mOptions;
    private ModIngameGui mIngameGui;
    private ModFirstPersonRenderer mItemInHandRenderer;
    private ModPlayerRenderer mPlayerRenderer;

    public ClientObjectHolder(Minecraft minecraft) {
        instance = this;
        this.minecraft = minecraft;
    }

    public void setup() {
        this.mOptions = new ModGameSettings(this.minecraft, this.minecraft.gameDirectory);
        this.mIngameGui = new ModIngameGui(this.minecraft);
        this.mItemInHandRenderer = new ModFirstPersonRenderer(this.minecraft);
        this.mPlayerRenderer = new ModPlayerRenderer(this.minecraft.getEntityRenderDispatcher());
    }

    public @NotNull ModGameSettings getMOptions() {
        return this.mOptions;
    }

    public @NotNull ModIngameGui getMIngameGui() {
        return this.mIngameGui;
    }

    public @NotNull ModFirstPersonRenderer getMItemInHandRenderer() {
        return this.mItemInHandRenderer;
    }

    public @NotNull ModPlayerRenderer getMPlayerRenderer() {
        return this.mPlayerRenderer;
    }

    public static ClientObjectHolder getInstance() {
        return instance;
    }
}
