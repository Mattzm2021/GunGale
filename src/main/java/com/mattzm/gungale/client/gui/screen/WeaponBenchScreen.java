package com.mattzm.gungale.client.gui.screen;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.inventory.container.WeaponBenchContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;

public class WeaponBenchScreen extends ContainerScreen<WeaponBenchContainer> implements IRecipeShownListener {
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(GunGale.MOD_ID, "textures/gui/container/weapon_bench.png");
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    private final RecipeBookGui recipeBookComponent = new RecipeBookGui();
    private boolean widthTooNarrow;

    public WeaponBenchScreen(WeaponBenchContainer container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        if (this.minecraft != null) {
            this.widthTooNarrow = this.width < 379;
            this.recipeBookComponent.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
            this.leftPos = this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
            this.children.add(this.recipeBookComponent);
            this.setInitialFocus(this.recipeBookComponent);
            this.addButton(new ImageButton(this.leftPos + 5, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_LOCATION, (button) -> {
                this.recipeBookComponent.initVisuals(this.widthTooNarrow);
                this.recipeBookComponent.toggleVisibility();
                this.leftPos = this.recipeBookComponent.updateScreenPosition(this.widthTooNarrow, this.width, this.imageWidth);
                ((ImageButton) button).setPosition(this.leftPos + 5, this.height / 2 - 49);
            }));
            this.titleLabelX = 11;
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.recipeBookComponent.tick();
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        if (this.recipeBookComponent.isVisible() && this.widthTooNarrow) {
            this.renderBg(matrixStack, partialTicks, mouseX, mouseY);
            this.recipeBookComponent.render(matrixStack, mouseX, mouseY, partialTicks);
        } else {
            this.recipeBookComponent.render(matrixStack, mouseX, mouseY, partialTicks);
            super.render(matrixStack, mouseX, mouseY, partialTicks);
            this.recipeBookComponent.renderGhostRecipe(matrixStack, this.leftPos, this.topPos, true, partialTicks);
        }

        this.renderTooltip(matrixStack, mouseX, mouseY);
        this.recipeBookComponent.renderTooltip(matrixStack, this.leftPos, this.topPos, mouseX, mouseY);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void renderBg(@NotNull MatrixStack stack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        if (this.minecraft != null) {
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.minecraft.textureManager.bind(CONTAINER_BACKGROUND);
            int x = (this.width - this.imageWidth) / 2;
            int y = (this.height - this.imageHeight) / 2;
            this.blit(stack, x, y, 0, 0, this.imageWidth, this.imageHeight);
        }
    }

    @Override
    protected boolean isHovering(int slotX, int slotY, int p_195359_3_, int p_195359_4_, double mouseX, double mouseY) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(slotX, slotY, p_195359_3_, p_195359_4_, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        if (this.recipeBookComponent.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
        }
    }

    @Override
    protected boolean hasClickedOutside(double p_195361_1_, double p_195361_3_, int p_195361_5_, int p_195361_6_, int p_195361_7_) {
        boolean flag = p_195361_1_ < (double)p_195361_5_ || p_195361_3_ < (double)p_195361_6_ || p_195361_1_ >= (double)(p_195361_5_ + this.imageWidth) || p_195361_3_ >= (double)(p_195361_6_ + this.imageHeight);
        return this.recipeBookComponent.hasClickedOutside(p_195361_1_, p_195361_3_, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, p_195361_7_) && flag;
    }

    @Override
    protected void slotClicked(@NotNull Slot p_184098_1_, int p_184098_2_, int p_184098_3_, @NotNull ClickType p_184098_4_) {
        super.slotClicked(p_184098_1_, p_184098_2_, p_184098_3_, p_184098_4_);
        this.recipeBookComponent.slotClicked(p_184098_1_);
    }

    @Override
    public void recipesUpdated() {
        this.recipeBookComponent.recipesUpdated();
    }

    @Override
    public void removed() {
        this.recipeBookComponent.removed();
        super.removed();
    }

    @Override
    public @NotNull RecipeBookGui getRecipeBookComponent() {
        return this.recipeBookComponent;
    }
}
