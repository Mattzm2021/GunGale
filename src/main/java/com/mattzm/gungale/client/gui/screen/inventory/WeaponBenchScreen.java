package com.mattzm.gungale.client.gui.screen.inventory;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.inventory.container.WeaponBenchContainer;
import com.mattzm.gungale.util.VanillaCode;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@VanillaCode("CraftingScreen")
@OnlyIn(Dist.CLIENT)
public class WeaponBenchScreen extends ContainerScreen<WeaponBenchContainer> {
    private static final ResourceLocation WEAPON_BENCH_LOCATION = new ResourceLocation(GunGale.MOD_ID, "textures/gui/container/weapon_bench.png");

    public WeaponBenchScreen(WeaponBenchContainer container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = 11;
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void renderBg(@NotNull MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        if (this.minecraft == null) return;
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.textureManager.bind(WEAPON_BENCH_LOCATION);
        int x = this.leftPos;
        int y = (this.height - this.imageHeight) / 2;
        this.blit(stack, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }
}
