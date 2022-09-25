package com.mattzm.gungale.client.gui.screen;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.client.settings.ModSettings;
import com.mattzm.gungale.inventory.container.ModWorkbenchContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;

public class ModCraftingScreen extends ContainerScreen<ModWorkbenchContainer> implements IRecipeShownListener {
    private static final ResourceLocation MOD_CRAFTING_TABLE_LOCATION = new ResourceLocation(GunGale.MOD_ID, "textures/gui/container/mod_crafting_table.png");
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    private final RecipeBookGui recipeBookComponent = new RecipeBookGui();
    private boolean widthTooNarrow;
    private boolean modActive = false;

    public ModCraftingScreen(ModWorkbenchContainer container, PlayerInventory inventory, ITextComponent text) {
        super(container, inventory, text);
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
            this.titleLabelX = 29;
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
    protected void renderBg(@NotNull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.minecraft != null) {
            this.minecraft.getTextureManager().bind(MOD_CRAFTING_TABLE_LOCATION);
            blit(matrixStack, this.leftPos, (this.height - this.imageHeight) / 2, this.modActive ? this.imageWidth : 0, 0, this.imageWidth, this.imageHeight, 512, 512);
        }
    }

    @Override
    protected void renderLabels(@NotNull MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        if (this.modActive) {
            this.font.draw(p_230451_1_, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
            this.font.draw(p_230451_1_, this.menu.inventory.getDisplayName(), (float) this.inventoryLabelX, (float) this.inventoryLabelY, 4210752);
        } else {
            super.renderLabels(p_230451_1_, p_230451_2_, p_230451_3_);
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

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        InputMappings.Input mouseKey = InputMappings.getKey(p_231046_1_, p_231046_2_);
        if (this.minecraft == null) {
            return false;
        } else if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey) && this.modActive) {
            this.setToDefault();
            return true;
        } else if (super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_)) {
            return true;
        } else if (ModSettings.KEY_INVENTORY.isActiveAndMatches(mouseKey) && !this.modActive) {
            this.setToMod();
            return true;
        } else {
            return false;
        }
    }

    private void setToDefault() {
        this.modActive = false;
        this.menu.setToDefault();
    }

    private void setToMod() {
        this.modActive = true;
        this.menu.setToMod();
    }
}
