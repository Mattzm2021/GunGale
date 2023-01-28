package com.mattzm.gungale.client.gui.screen;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.client.settings.ModSettings;
import com.mattzm.gungale.inventory.container.ModItemContainer;
import com.mattzm.gungale.util.VanillaCode;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@VanillaCode("InventoryScreen")
@OnlyIn(Dist.CLIENT)
public class ModInventoryScreen extends ContainerScreen<ModItemContainer> {
    private static final ResourceLocation MOD_INVENTORY_LOCATION = new ResourceLocation(GunGale.MOD_ID, "textures/gui/container/mod_inventory.png");

    public ModInventoryScreen(ModItemContainer p_i51105_1_, PlayerInventory p_i51105_2_, ITextComponent p_i51105_3_) {
        super(p_i51105_1_, p_i51105_2_, p_i51105_3_);
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        InputMappings.Input mouseKey = InputMappings.getKey(p_231046_1_, p_231046_2_);
        if (this.minecraft == null) {
            return false;
        } else if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            this.onClose();
            if (this.minecraft.player != null && this.minecraft.gameMode != null) {
                if (this.minecraft.gameMode.hasInfiniteItems()) {
                    this.minecraft.setScreen(new CreativeScreen(this.minecraft.player));
                } else {
                    this.minecraft.setScreen(new InventoryScreen(this.minecraft.player));
                }
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
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@NotNull MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {}

    @SuppressWarnings("deprecation")
    @Override
    protected void renderBg(@NotNull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        if (this.minecraft == null) return;
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.textureManager.bind(MOD_INVENTORY_LOCATION);
        int x = this.leftPos;
        int y = this.topPos;
        this.blit(matrixStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }
}
