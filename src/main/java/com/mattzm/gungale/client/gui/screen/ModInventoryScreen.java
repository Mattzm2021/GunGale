package com.mattzm.gungale.client.gui.screen;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.client.settings.ModSettings;
import com.mattzm.gungale.inventory.container.ModItemContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

public class ModInventoryScreen extends ContainerScreen<ModItemContainer> {
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(GunGale.MOD_ID, "textures/gui/container/mod_inventory.png");

    protected ModInventoryScreen(ModItemContainer p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    @Override
    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {


        return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        InputMappings.Input mouseKey = InputMappings.getKey(p_231046_1_, p_231046_2_);
        if (this.minecraft == null) {
            return false;
        } else if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            this.onClose();
            if (this.minecraft.player != null) {
                this.minecraft.setScreen(new InventoryScreen(this.minecraft.player));
            }

            return true;
        } else if (super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_)) {
            return true;
        } else if (ModSettings.KEY_INVENTORY.isActiveAndMatches(mouseKey)) {
            this.onClose();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderTooltip(matrixStack, p_230430_2_, p_230430_3_);
    }

    @Override
    protected void renderLabels(@NotNull MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {}

    @Override
    protected void renderBg(@NotNull MatrixStack p_230450_1_, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        if (this.minecraft != null) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.minecraft.textureManager.bind(CONTAINER_BACKGROUND);
            int x = (this.width - this.imageWidth) / 2;
            int y = (this.height - this.imageHeight) / 2;
            this.blit(p_230450_1_, x, y, 0, 0, this.imageWidth, this.imageHeight);
        }
    }
}
