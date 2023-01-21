package com.mattzm.gungale.client.gui;

import com.mattzm.gungale.GunGale;
import com.mattzm.gungale.client.nbt.tick.RestoreNBT;
import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mattzm.gungale.item.GearItem;
import com.mattzm.gungale.item.ModItems;
import com.mattzm.gungale.item.OpticItem;
import com.mattzm.gungale.item.weapon.AbstractWeaponItem;
import com.mattzm.gungale.nbt.stack.BulletNBT;
import com.mattzm.gungale.client.nbt.tick.ReloadNBT;
import com.mattzm.gungale.nbt.stack.ShieldEvolveNBT;
import com.mattzm.gungale.util.color.ColoredText;
import com.mattzm.gungale.util.math.ModMathHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.GameSettings;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.settings.AttackIndicatorStatus;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ModIngameGui {
    public boolean showBorderText = false;
    private static final ResourceLocation MOD_ICONS_LOCATION = new ResourceLocation(GunGale.MOD_ID, "textures/gui/icons.png");
    private static final ResourceLocation MOD_WIDGETS_LOCATION = new ResourceLocation(GunGale.MOD_ID, "textures/gui/widgets.png");
    private static final Logger LOGGER = LogManager.getLogger();
    private final Minecraft minecraft;
    private final FontRenderer font;
    private final MainWindow window;
    private final GameSettings options;
    private final IngameGui gui;
    private final ItemRenderer itemRenderer;
    private int toolHighlightTimer;
    private ItemStack lastToolHighlight = ItemStack.EMPTY;
    private ColoredText[] damageTexts = new ColoredText[5];
    private float showDamageTick;
    private Vector2f[] damagePoses = new Vector2f[5];

    public ModIngameGui(@NotNull Minecraft minecraft) {
        this.minecraft = minecraft;
        this.font = minecraft.font;
        this.window = minecraft.getWindow();
        this.options = minecraft.options;
        this.gui = minecraft.gui;
        this.itemRenderer = minecraft.getItemRenderer();
    }

    public void render(MatrixStack matrixStack, float partialTick) {
        float f = this.showDamageTick;
        if (f > 0) {
            if (Integer.parseInt(this.minecraft.fpsString.split(" ")[0]) > 20) {
                if (partialTick + ModMathHelper.getFloatPart(f) < 1.0f) {
                    f = MathHelper.floor(f) - partialTick;
                } else {
                    f = MathHelper.ceil(f) - partialTick;
                }
            } else {
                f--;
            }
        } else {
            f = 0;
            this.damageTexts = new ColoredText[5];
            this.damagePoses = new Vector2f[5];
        }

        if (f > 0) {
            this.renderDamageText(matrixStack, f);
        }

        this.showDamageTick = f;
    }

    public void tick() {
        if (this.minecraft.player == null) return;
        ModPlayerInventory inventory = ModPlayerInventory.get(this.minecraft.player);
        ItemStack stack = inventory.status ? inventory.getSelected() : this.minecraft.player.inventory.getSelected();
        if (stack.isEmpty()) {
            this.toolHighlightTimer = 0;
        } else if (!this.lastToolHighlight.isEmpty() && stack.getItem() == this.lastToolHighlight.getItem() && (stack.getHoverName().equals(this.lastToolHighlight.getHoverName()) && stack.getHighlightTip(stack.getHoverName()).equals(lastToolHighlight.getHighlightTip(lastToolHighlight.getHoverName())))) {
            if (this.toolHighlightTimer > 0) {
                this.toolHighlightTimer--;
            }
        } else {
            this.toolHighlightTimer = 40;
        }

        this.lastToolHighlight = stack;
    }

    public void renderBulletText(MatrixStack matrixStack) {
        PlayerEntity player = this.getPlayer();
        if (player == null) return;
        ItemStack stack = ModPlayerInventory.get(player).getSelected();
        AbstractWeaponItem item = (AbstractWeaponItem) stack.getItem();
        String text = player.isSpectator() ? null
                : player.isCreative() ? "-- / --"
                : BulletNBT.get(stack) + " / " + item.getAllBullet(player);
        if (text == null) return;
        float posX = (this.getWidth() - this.font.width(text)) / 2.0F;
        float posY = this.getHeight() - (player.isCreative() ? 35.0F : 48.0F);
        this.font.draw(matrixStack, text, posX + 1.0F, posY, 0);
        this.font.draw(matrixStack, text, posX - 1.0F, posY, 0);
        this.font.draw(matrixStack, text, posX, posY + 1.0F, 0);
        this.font.draw(matrixStack, text, posX, posY - 1.0F, 0);
        this.font.draw(matrixStack, text, posX, posY, 8453920);
    }

    public void renderReloadTickText(MatrixStack matrixStack) {
        PlayerEntity player = this.getPlayer();
        if (player == null) return;
        String text = ReloadNBT.hasStart(player)
                ? ModMathHelper.oneDigitFloat(ModMathHelper.tickToSecond(ReloadNBT.getRealSpace(player))) + "s"
                : null;
        if (text == null) return;
        float posX = (this.getWidth() - this.font.width(text)) / 2.0F + 50.0F;
        float posY = this.getHeight() - (player.isCreative() ? 35.0F : 48.0F);
        this.font.draw(matrixStack, text, posX + 1.0F, posY, 0);
        this.font.draw(matrixStack, text, posX - 1.0F, posY, 0);
        this.font.draw(matrixStack, text, posX, posY + 1.0F, 0);
        this.font.draw(matrixStack, text, posX, posY - 1.0F, 0);
        this.font.draw(matrixStack, text, posX, posY, 8453920);
    }

    public void renderRestoreTickText(MatrixStack matrixStack) {
        PlayerEntity player = this.getPlayer();
        if (player == null) return;
        String text = RestoreNBT.hasStart(this.getPlayer())
                ? ModMathHelper.oneDigitFloat(ModMathHelper.tickToSecond(RestoreNBT.getRealSpace(this.getPlayer()))) + "s"
                : null;
        if (text == null) return;
        float posX = (this.getWidth() - this.font.width(text)) / 2.0F + 50.0F;
        float posY = this.getHeight() - (player.isCreative() ? 35.0F : 48.0F);
        this.font.draw(matrixStack, text, posX + 1.0F, posY, 0);
        this.font.draw(matrixStack, text, posX - 1.0F, posY, 0);
        this.font.draw(matrixStack, text, posX, posY + 1.0F, 0);
        this.font.draw(matrixStack, text, posX, posY - 1.0F, 0);
        this.font.draw(matrixStack, text, posX, posY, 8453920);
    }

    public void renderEvolveAmountText(MatrixStack matrixStack) {
        if (this.getPlayer() == null) return;
        String text = GearItem.hasGear(this.getPlayer())
                ? ((GearItem) GearItem.get(this.getPlayer()).getItem()).isEvolvable()
                    ? ModMathHelper.twoDigitsDouble(ShieldEvolveNBT.get(GearItem.get(this.getPlayer())) / 2) + " Next Evolve"
                    : "Max evolve"
                : null;
        if (text == null) return;
        float posX = (this.getWidth() - this.font.width(text)) / 2.0F - 150.0f;
        float posY = this.getHeight() - 10.0f;
        this.font.draw(matrixStack, text, posX + 1.0F, posY, 0);
        this.font.draw(matrixStack, text, posX - 1.0F, posY, 0);
        this.font.draw(matrixStack, text, posX, posY + 1.0F, 0);
        this.font.draw(matrixStack, text, posX, posY - 1.0F, 0);
        this.font.draw(matrixStack, text, posX, posY, 8453920);
    }

    public void renderBorderText(MatrixStack matrixStack) {
        if (this.getPlayer() == null) return;
        String text = "Border: " + ModMathHelper.twoDigitsDouble(this.getPlayer().level.getWorldBorder().getDistanceToBorder(this.getPlayer()));
        float posX = (this.getWidth() - this.font.width(text)) / 2.0F - 150.0F;
        float posY = 10.0F;
        this.font.draw(matrixStack, text, posX + 1.0F, posY, 0);
        this.font.draw(matrixStack, text, posX - 1.0F, posY, 0);
        this.font.draw(matrixStack, text, posX, posY + 1.0F, 0);
        this.font.draw(matrixStack, text, posX, posY - 1.0F, 0);
        this.font.draw(matrixStack, text, posX, posY, 8453920);
    }

    public void renderDamageText(MatrixStack matrixStack, float tick) {
        for (int i = 0; i < this.damageTexts.length; i++) {
            if (this.getPlayer() != null && this.damageTexts[i] != null) {
                GL11.glPushMatrix();
                GL11.glScalef(this.damageTexts[i].size, this.damageTexts[i].size, this.damageTexts[i].size);
                Vector2f currViewVec = new Vector2f(this.getPlayer().xRot, this.getPlayer().yRot);
                double verticalFov = this.options.fov;
                double horizontalFov = ModMathHelper.VFovToHFov(verticalFov);
                float posX = (this.getWidth() - this.font.width(this.damageTexts[i].text)) * (0.5f - (currViewVec.y - this.damagePoses[i].y) / (float) horizontalFov) / this.damageTexts[i].size;
                float posY = (this.getHeight() * (0.5f - (currViewVec.x - this.damagePoses[i].x) / (float) verticalFov) - 20.0f + tick - this.damageTexts[i].margin) / this.damageTexts[i].size;
                this.font.draw(matrixStack, this.damageTexts[i].text, posX + 0.6f, posY, ColoredText.toInt(this.damageTexts[i].outer));
                this.font.draw(matrixStack, this.damageTexts[i].text, posX - 0.6f, posY, ColoredText.toInt(this.damageTexts[i].outer));
                this.font.draw(matrixStack, this.damageTexts[i].text, posX, posY + 0.6f, ColoredText.toInt(this.damageTexts[i].outer));
                this.font.draw(matrixStack, this.damageTexts[i].text, posX, posY - 0.6f, ColoredText.toInt(this.damageTexts[i].outer));
                this.font.draw(matrixStack, this.damageTexts[i].text, posX, posY, ColoredText.toInt(this.damageTexts[i].inner));
                GL11.glPopMatrix();
            }
        }
    }


    public void renderAds(OpticItem item, MatrixStack matrixStack) {
        if (item == null) {
            renderAdsIronSight(matrixStack);
        } else if (item == ModItems.HCOG_CLASSIC) {
            renderAdsClassic(matrixStack);
        } else if (item == ModItems.HOLO) {
            renderAdsHolo(matrixStack);
        } else if (item == ModItems.HCOG_BRUISER) {
            renderAdsBruiser(matrixStack);
        } else if (item == ModItems.VARIABLE_HOLO) {
            renderAdsVariableHolo(matrixStack);
        } else if (item == ModItems.HCOG_RANGER) {
            renderAdsRanger(matrixStack);
        } else if (item == ModItems.VARIABLE_AOG) {
            renderAdsAOG(matrixStack);
        }
    }

    private void renderAdsIronSight(MatrixStack stack) {
        this.minecraft.getTextureManager().bind(MOD_ICONS_LOCATION);
        if (this.options.getCameraType().isFirstPerson()) {
            if (this.minecraft.gameMode == null) LOGGER.error("renderAdsFrontSight cannot provide a PlayerController!");
            else if (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
                if (this.getPlayer() == null) return;
                blit(stack, (getWidth() - 15) / 2, (getHeight() - 15) / 2, 0, 0, 15, 15, 256, 256);
            }
        }
    }

    private void renderAdsClassic(MatrixStack stack) {
        this.minecraft.getTextureManager().bind(new ResourceLocation(GunGale.MOD_ID, "textures/gui/optic/hcog_classic.png"));
        if (this.options.getCameraType().isFirstPerson()) {
            if (this.minecraft.gameMode != null) {
                if (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR && this.getPlayer() != null) {
                    blit(stack, (getWidth() - 59) / 2, (getHeight() - 59) / 2, 0, 0, 59, 59, 59, 59);
                }
            }
        }
    }

    private void renderAdsHolo(MatrixStack stack) {
        this.minecraft.getTextureManager().bind(new ResourceLocation(GunGale.MOD_ID, "textures/gui/optic/hcog_classic.png"));
        if (this.options.getCameraType().isFirstPerson()) {
            if (this.minecraft.gameMode != null) {
                if (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR && this.getPlayer() != null) {
                    blit(stack, (getWidth() - 59) / 2, (getHeight() - 59) / 2, 0, 0, 59, 59, 59, 59);
                }
            }
        }
    }

    private void renderAdsBruiser(MatrixStack stack) {
        this.minecraft.getTextureManager().bind(new ResourceLocation(GunGale.MOD_ID, "textures/gui/optic/hcog_bruiser.png"));
        if (this.options.getCameraType().isFirstPerson()) {
            if (this.minecraft.gameMode != null) {
                if (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR && this.getPlayer() != null) {
                    blit(stack, (getWidth() - 99) / 2, (getHeight() - 99) / 2, 0, 0, 99, 99, 99, 99);
                }
            }
        }
    }

    private void renderAdsVariableHolo(MatrixStack stack) {
        this.minecraft.getTextureManager().bind(new ResourceLocation(GunGale.MOD_ID, "textures/gui/optic/hcog_classic.png"));
        if (this.options.getCameraType().isFirstPerson()) {
            if (this.minecraft.gameMode != null) {
                if (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR && this.getPlayer() != null) {
                    blit(stack, (getWidth() - 59) / 2, (getHeight() - 59) / 2, 0, 0, 59, 59, 59, 59);
                }
            }
        }
    }

    private void renderAdsRanger(MatrixStack stack) {
        this.minecraft.getTextureManager().bind(new ResourceLocation(GunGale.MOD_ID, "textures/gui/optic/hcog_classic.png"));
        if (this.options.getCameraType().isFirstPerson()) {
            if (this.minecraft.gameMode != null) {
                if (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR && this.getPlayer() != null) {
                    blit(stack, (getWidth() - 59) / 2, (getHeight() - 59) / 2, 0, 0, 59, 59, 59, 59);
                }
            }
        }
    }

    private void renderAdsAOG(MatrixStack stack) {
        this.minecraft.getTextureManager().bind(new ResourceLocation(GunGale.MOD_ID, "textures/gui/optic/hcog_classic.png"));
        if (this.options.getCameraType().isFirstPerson()) {
            if (this.minecraft.gameMode != null) {
                if (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR && this.getPlayer() != null) {
                    blit(stack, (getWidth() - 59) / 2, (getHeight() - 59) / 2, 0, 0, 59, 59, 59, 59);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void renderHotbar(float partialTicks, MatrixStack matrixStack) {
        PlayerEntity player = this.getCameraPlayer();
        if (player == null) return;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(MOD_WIDGETS_LOCATION);
        ModPlayerInventory inventory = ModPlayerInventory.get(player);
        ItemStack stack = player.getOffhandItem();
        HandSide offHand = player.getMainArm().getOpposite();
        int posX = this.getWidth() / 2;
        int blitOffset = this.getBlitOffset();
        this.setBlitOffset(-90);
        this.blit(matrixStack, posX - 91, this.getHeight() - 22, 0, 0, 182, 22);
        this.blit(matrixStack, offHand == HandSide.LEFT ? posX + 91 : posX - 91 - 49, this.getHeight() - 22, offHand == HandSide.LEFT ? 49 : 0, 206, 49, 22);

        if (inventory.status) {
            int baseX = offHand == HandSide.LEFT ? posX + 91 + 7 - 1 : posX - 91 - 49 - 1;
            this.blit(matrixStack, baseX + inventory.selected / 6 * 20, getHeight() - 22 - 1, 0, 22, 24, 22);
        } else {
            this.blit(matrixStack, posX - 91 - 1 + player.inventory.selected * 20, this.getHeight() - 22 - 1, 0, 22, 24, 22);
        }

        if (!stack.isEmpty()) {
            if (offHand == HandSide.LEFT) {
                this.blit(matrixStack, posX - 91 - 29, this.getHeight() - 23, 24, 22, 29, 24);
            } else {
                this.blit(matrixStack, posX + 91, this.getHeight() - 23, 53, 22, 29, 24);
            }
        }

        this.setBlitOffset(blitOffset);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        for (int i = 0; i < 9; i++) {
            int posX1 = posX - 90 + i * 20 + 2;
            int posY = this.getHeight() - 16 - 3;
            this.renderSlot(posX1, posY, partialTicks, player, player.inventory.items.get(i));
        }

        for (int i = 0; i < 2; i++) {
            int baseX1 = offHand == HandSide.LEFT ? posX + 91 + 7 + 3 : posX - 91 - 49 + 3;
            this.renderSlot(baseX1 + i * 20, this.getHeight() - 16 - 3, partialTicks, player, inventory.getItem(i * 6));
        }

        if (!stack.isEmpty()) {
            int posY1 = this.getHeight() - 16 - 3;
            if (offHand == HandSide.LEFT) {
                this.renderSlot(posX - 91 - 26, posY1, partialTicks, player, stack);
            } else {
                this.renderSlot(posX + 91 + 10, posY1, partialTicks, player, stack);
            }
        }

        if (this.minecraft.options.attackIndicator == AttackIndicatorStatus.HOTBAR && this.minecraft.player != null) {
            float scale = this.minecraft.player.getAttackStrengthScale(0.0F);
            if (scale < 1.0F) {
                int posY2 = this.getHeight() - 20;
                int posX2 = posX + 91 + 6;
                if (offHand == HandSide.RIGHT) {
                    posX2 = posX - 91 - 22;
                }

                this.minecraft.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
                int scale1 = (int) (scale * 19.0F);
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.blit(matrixStack, posX2, posY2, 0, 94, 18, 18);
                this.blit(matrixStack, posX2, posY2 + 18 - scale1, 18, 112 - scale1, 18, scale1);
            }
        }

        RenderSystem.disableRescaleNormal();
        RenderSystem.disableBlend();
    }

    private void renderSlot(int posX, int posY, float partialTicks, PlayerEntity player, @NotNull ItemStack stack) {
        if (stack.isEmpty()) return;
        float f = (float) stack.getPopTime() - partialTicks;
        if (f > 0.0F) {
            GL11.glPushMatrix();
            float f1 = 1.0F + f / 5.0F;
            GL11.glTranslatef((float) (posX + 8), (float) (posY + 12), 0.0F);
            GL11.glScalef(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
            GL11.glTranslatef((float) (-(posX + 8)), (float) (-(posY + 12)), 0.0F);
        }

        this.itemRenderer.renderAndDecorateItem(player, stack, posX, posY);
        if (f > 0.0F) {
            GL11.glPopMatrix();
        }

        this.itemRenderer.renderGuiItemDecorations(this.minecraft.font, stack, posX, posY);
    }

    public void renderSelectedItemName(MatrixStack matrixStack) {
        this.minecraft.getProfiler().push("selectedItemName");
        if (this.toolHighlightTimer > 0 && !this.lastToolHighlight.isEmpty()) {
            IFormattableTextComponent iformattabletextcomponent = (new StringTextComponent("")).append(this.lastToolHighlight.getHoverName()).withStyle(this.lastToolHighlight.getRarity().color);
            if (this.lastToolHighlight.hasCustomHoverName()) {
                iformattabletextcomponent.withStyle(TextFormatting.ITALIC);
            }

            ITextComponent highlightTip = this.lastToolHighlight.getHighlightTip(iformattabletextcomponent);
            int i = this.font.width(highlightTip);
            int j = (this.getWidth() - i) / 2;
            int k = this.getHeight() - 59;
            if (this.minecraft.gameMode != null && !this.minecraft.gameMode.canHurtPlayer()) {
                k += 14;
            }

            int l = (int)((float)this.toolHighlightTimer * 256.0F / 10.0F);
            if (l > 255) {
                l = 255;
            }

            if (l > 0) {
                GL11.glPushMatrix();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                AbstractGui.fill(matrixStack, j - 2, k - 2, j + i + 2, k + 9 + 2, this.minecraft.options.getBackgroundColor(0));
                FontRenderer font = lastToolHighlight.getItem().getFontRenderer(lastToolHighlight);
                if (font == null) {
                    this.font.drawShadow(matrixStack, highlightTip, (float)j, (float)k, 16777215 + (l << 24));
                } else {
                    j = (this.getWidth() - font.width(highlightTip)) / 2;
                    font.drawShadow(matrixStack, highlightTip, (float)j, (float)k, 16777215 + (l << 24));
                }
                RenderSystem.disableBlend();
                GL11.glPopMatrix();
            }
        }

        this.minecraft.getProfiler().pop();
    }

    private int getWidth() {
        return this.window.getGuiScaledWidth();
    }

    private int getHeight() {
        return this.window.getGuiScaledHeight();
    }

    private void setBlitOffset(int value) {
        this.gui.setBlitOffset(value);
    }

    private int getBlitOffset() {
        return this.gui.getBlitOffset();
    }

    private @Nullable ClientPlayerEntity getPlayer() {
        return this.minecraft.player;
    }
    
    private @Nullable PlayerEntity getCameraPlayer() {
        return this.minecraft.getCameraEntity() instanceof PlayerEntity ? (PlayerEntity) this.minecraft.getCameraEntity() : null;
    }

    public void blit(MatrixStack stack, int posX, int posY, int startX, int startY, int width, int height) {
        this.gui.blit(stack, posX, posY, startX, startY, width, height);
    }

    public void blit(MatrixStack stack, int posX, int posY, int startX, int startY, int width, int height, int textureWidth, int textureHeight) {
        AbstractGui.blit(stack, posX, posY, this.getBlitOffset(), startX, startY, width, height, textureWidth, textureHeight);
    }

    public ColoredText[] getDamageTexts() {
        return this.damageTexts;
    }

    public Vector2f[] getDamagePoses() {
        return this.damagePoses;
    }

    public void setMainDamagePos(float f1, float f2) {
        this.damagePoses[0] = new Vector2f(f1, f2);
    }

    public void setMainDamageText(@NotNull ColoredText text) {
        this.damageTexts[0] = text;
        this.showDamageTick = 20.0f;
    }
}
