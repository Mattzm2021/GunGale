package com.mattzm.gungale.client.gui.screen.inventory;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.client.settings.ModGameSettings;
import com.mattzm.gungale.inventory.container.ModWorkbenchContainer;
import com.mattzm.gungale.util.VanillaCode;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@VanillaCode("CraftingScreen")
@OnlyIn(Dist.CLIENT)
public class ModCraftingScreen extends ContainerScreen<ModWorkbenchContainer> implements IRecipeShownListener {
    private static final ResourceLocation MOD_CRAFTING_TABLE_LOCATION = new ResourceLocation(GunGale.MOD_ID, "textures/gui/container/mod_crafting_table.png");
    private static final ResourceLocation RECIPE_BUTTON_LOCATION = new ResourceLocation("textures/gui/recipe_button.png");
    private final RecipeBookGui recipeBookComponent = new RecipeBookGui();
    private boolean widthTooNarrow;
    private boolean modActive = false;

    public ModCraftingScreen(ModWorkbenchContainer container, PlayerInventory inventory, ITextComponent component) {
        super(container, inventory, component);
    }

    @Override
    protected void init() {
        super.init();
        if (this.minecraft == null) return;
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
        if (this.minecraft == null) return;
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.textureManager.bind(MOD_CRAFTING_TABLE_LOCATION);
        int x = this.leftPos;
        int y = (this.height - this.imageHeight) / 2;
        blit(matrixStack, x, y, this.modActive ? this.imageWidth : 0, 0, this.imageWidth, this.imageHeight, 512, 512);
    }

    @Override
    protected void renderLabels(@NotNull MatrixStack matrixStack, int mouseX, int mouseY) {
        if (this.modActive) {
            this.font.draw(matrixStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
            this.font.draw(matrixStack, this.menu.inventory.getDisplayName(), (float) this.inventoryLabelX, (float) this.inventoryLabelY, 4210752);
        } else {
            super.renderLabels(matrixStack, mouseX, mouseY);
        }
    }

    @Override
    protected boolean isHovering(int slotX, int slotY, int gridWidth, int gridHeight, double mouseX, double mouseY) {
        return (!this.widthTooNarrow || !this.recipeBookComponent.isVisible()) && super.isHovering(slotX, slotY, gridWidth, gridHeight, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int buttonId) {
        if (this.recipeBookComponent.mouseClicked(mouseX, mouseY, buttonId)) {
            this.setFocused(this.recipeBookComponent);
            return true;
        } else {
            return this.widthTooNarrow && this.recipeBookComponent.isVisible() || super.mouseClicked(mouseX, mouseY, buttonId);
        }
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int leftPos, int topPos, int buttonId) {
        boolean flag = mouseX < (double) leftPos || mouseY < (double) topPos || mouseX >= (double) (leftPos + this.imageWidth) || mouseY >= (double) (topPos + this.imageHeight);
        return this.recipeBookComponent.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, buttonId) && flag;
    }

    @Override
    protected void slotClicked(@NotNull Slot slot, int slotId, int buttonId, @NotNull ClickType clickType) {
        super.slotClicked(slot, slotId, buttonId, clickType);
        this.recipeBookComponent.slotClicked(slot);
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
    public boolean keyPressed(int buttonId, int scancode, int modifier) {
        InputMappings.Input mouseKey = InputMappings.getKey(buttonId, scancode);
        if (this.minecraft == null) {
            return false;
        } else if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey) && this.modActive) {
            this.setToDefault();
            return true;
        } else if (super.keyPressed(buttonId, scancode, modifier)) {
            return true;
        } else if (ModGameSettings.KEY_INVENTORY.isActiveAndMatches(mouseKey) && !this.modActive) {
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
