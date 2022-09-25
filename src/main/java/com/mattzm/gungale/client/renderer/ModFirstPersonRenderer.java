package com.mattzm.gungale.client.renderer;

import com.google.common.base.MoreObjects;
import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ModFirstPersonRenderer {
    private static final RenderType MAP_BACKGROUND = RenderType.text(new ResourceLocation("textures/map/map_background.png"));
    private static final RenderType MAP_BACKGROUND_CHECKERBOARD = RenderType.text(new ResourceLocation("textures/map/map_background_checkerboard.png"));
    private final Minecraft minecraft;
    private ItemStack mainHandItem = ItemStack.EMPTY;
    private ItemStack offHandItem = ItemStack.EMPTY;
    private float mainHandHeight;
    private float oMainHandHeight;
    private float offHandHeight;
    private float oOffHandHeight;
    private final EntityRendererManager entityRenderDispatcher;
    private final ItemRenderer itemRenderer;

    public ModFirstPersonRenderer(@NotNull Minecraft minecraft) {
        this.minecraft = minecraft;
        this.entityRenderDispatcher = minecraft.getEntityRenderDispatcher();
        this.itemRenderer = minecraft.getItemRenderer();
    }

    public void renderItem(LivingEntity entity, @NotNull ItemStack stack, ItemCameraTransforms.TransformType transformType, boolean handSideFlag, MatrixStack matrixStack, IRenderTypeBuffer buffers, int light) {
        if (!stack.isEmpty()) {
            this.itemRenderer.renderStatic(entity, stack, transformType, handSideFlag, matrixStack, buffers, entity.level, light, OverlayTexture.NO_OVERLAY);
        }
    }

    private float calculateMapTilt(float interpPitch) {
        float f = 1.0F - interpPitch / 45.0F + 0.1F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        return -MathHelper.cos(f * (float) Math.PI) * 0.5F + 0.5F;
    }

    private void renderMapHand(@NotNull MatrixStack matrixStack, IRenderTypeBuffer buffers, int light, HandSide handSide) {
        if (this.minecraft.player != null) this.minecraft.getTextureManager().bind(this.minecraft.player.getSkinTextureLocation());
        PlayerRenderer playerrenderer = (PlayerRenderer) this.entityRenderDispatcher.<AbstractClientPlayerEntity>getRenderer(this.minecraft.player);
        matrixStack.pushPose();
        float f = handSide == HandSide.RIGHT ? 1.0F : -1.0F;
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(92.0F));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(45.0F));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(f * -41.0F));
        matrixStack.translate(f * 0.3F, -1.1F, 0.45F);
        if (handSide == HandSide.RIGHT) {
            playerrenderer.renderRightHand(matrixStack, buffers, light, this.minecraft.player);
        } else {
            playerrenderer.renderLeftHand(matrixStack, buffers, light, this.minecraft.player);
        }

        matrixStack.popPose();
    }

    private void renderOneHandedMap(@NotNull MatrixStack matrixStack, IRenderTypeBuffer buffers, int light, float equipProgress, HandSide handSide, float swingProgress, ItemStack stack) {
        float f = handSide == HandSide.RIGHT ? 1.0F : -1.0F;
        matrixStack.translate(f * 0.125F, -0.125D, 0.0D);
        if (this.minecraft.player != null && !this.minecraft.player.isInvisible()) {
            matrixStack.pushPose();
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(f * 10.0F));
            this.renderPlayerArm(matrixStack, buffers, light, equipProgress, swingProgress, handSide);
            matrixStack.popPose();
        }

        matrixStack.pushPose();
        matrixStack.translate(f * 0.51F, -0.08F + equipProgress * -1.2F, -0.75D);
        float f1 = MathHelper.sqrt(swingProgress);
        float f2 = MathHelper.sin(f1 * (float) Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f5 = -0.3F * MathHelper.sin(swingProgress * (float) Math.PI);
        matrixStack.translate(f * f3, f4 - 0.3F * f2, f5);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(f2 * -45.0F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(f * f2 * -30.0F));
        this.renderMap(matrixStack, buffers, light, stack);
        matrixStack.popPose();
    }

    private void renderTwoHandedMap(@NotNull MatrixStack matrixStack, IRenderTypeBuffer buffers, int light, float interpPitch, float equipProgress, float swingProgress) {
        float f = MathHelper.sqrt(swingProgress);
        float f1 = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
        float f2 = -0.4F * MathHelper.sin(f * (float) Math.PI);
        matrixStack.translate(0.0D, -f1 / 2.0F, f2);
        float f3 = this.calculateMapTilt(interpPitch);
        matrixStack.translate(0.0D, 0.04F + equipProgress * -1.2F + f3 * -0.5F, -0.72F);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(f3 * -85.0F));
        if (this.minecraft.player != null && !this.minecraft.player.isInvisible()) {
            matrixStack.pushPose();
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
            this.renderMapHand(matrixStack, buffers, light, HandSide.RIGHT);
            this.renderMapHand(matrixStack, buffers, light, HandSide.LEFT);
            matrixStack.popPose();
        }

        float f4 = MathHelper.sin(f * (float) Math.PI);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(f4 * 20.0F));
        matrixStack.scale(2.0F, 2.0F, 2.0F);
        this.renderMap(matrixStack, buffers, light, this.mainHandItem);
    }

    private void renderMap(@NotNull MatrixStack matrixStack, @NotNull IRenderTypeBuffer buffers, int light, ItemStack stack) {
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStack.scale(0.38F, 0.38F, 0.38F);
        matrixStack.translate(-0.5D, -0.5D, 0.0D);
        matrixStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
        MapData mapdata = this.minecraft.level == null ? null : FilledMapItem.getOrCreateSavedData(stack, this.minecraft.level);
        IVertexBuilder ivertexbuilder = buffers.getBuffer(mapdata == null ? MAP_BACKGROUND : MAP_BACKGROUND_CHECKERBOARD);
        Matrix4f matrix4f = matrixStack.last().pose();
        ivertexbuilder.vertex(matrix4f, -7.0F, 135.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(light).endVertex();
        ivertexbuilder.vertex(matrix4f, 135.0F, 135.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(light).endVertex();
        ivertexbuilder.vertex(matrix4f, 135.0F, -7.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(light).endVertex();
        ivertexbuilder.vertex(matrix4f, -7.0F, -7.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(light).endVertex();
        if (mapdata != null) {
            this.minecraft.gameRenderer.getMapRenderer().render(matrixStack, buffers, mapdata, false, light);
        }

    }

    private void renderPlayerArm(@NotNull MatrixStack matrixStack, IRenderTypeBuffer buffers, int light, float equipProgress, float swingProgress, HandSide handSide) {
        boolean flag = handSide != HandSide.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(swingProgress);
        float f2 = -0.3F * MathHelper.sin(f1 * (float) Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(swingProgress * (float) Math.PI);
        matrixStack.translate(f * (f2 + 0.64000005F), f3 - 0.6F + equipProgress * -0.6F, f4 - 0.71999997F);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(f * 45.0F));
        float f5 = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        float f6 = MathHelper.sin(f1 * (float) Math.PI);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(f * f6 * 70.0F));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(f * f5 * -20.0F));
        AbstractClientPlayerEntity player = this.minecraft.player;
        if (player != null) {
            this.minecraft.getTextureManager().bind(player.getSkinTextureLocation());
            matrixStack.translate(f * -1.0F, 3.6F, 3.5D);
            matrixStack.mulPose(Vector3f.ZP.rotationDegrees(f * 120.0F));
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(200.0F));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(f * -135.0F));
            matrixStack.translate(f * 5.6F, 0.0D, 0.0D);
            PlayerRenderer playerrenderer = (PlayerRenderer) this.entityRenderDispatcher.getRenderer(player);
            if (flag) {
                playerrenderer.renderRightHand(matrixStack, buffers, light, player);
            } else {
                playerrenderer.renderLeftHand(matrixStack, buffers, light, player);
            }
        }
    }

    private void applyEatTransform(MatrixStack matrixStack, float partialTicks, HandSide handSide, @NotNull ItemStack stack) {
        float f = this.minecraft.player == null ? 0.0F : (float) this.minecraft.player.getUseItemRemainingTicks() - partialTicks + 1.0F;
        float f1 = f / (float)stack.getUseDuration();
        if (f1 < 0.8F) {
            float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * (float)Math.PI) * 0.1F);
            matrixStack.translate(0.0D, f2, 0.0D);
        }

        float f3 = 1.0F - (float)Math.pow(f1, 27.0D);
        int i = handSide == HandSide.RIGHT ? 1 : -1;
        matrixStack.translate(f3 * 0.6F * (float) i, f3 * -0.5F, f3 * 0.0F);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) i * f3 * 90.0F));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(f3 * 10.0F));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float) i * f3 * 30.0F));
    }

    private void applyItemArmAttackTransform(@NotNull MatrixStack matrixStack, HandSide handSide, float swingProgress) {
        int i = handSide == HandSide.RIGHT ? 1 : -1;
        float f = MathHelper.sin(swingProgress * swingProgress * (float) Math.PI);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) i * (45.0F + f * -20.0F)));
        float f1 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float) i * f1 * -20.0F));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(f1 * -80.0F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) i * -45.0F));
    }

    private void applyItemArmTransform(@NotNull MatrixStack matrixStack, HandSide handSide, float equipProgress) {
        int i = handSide == HandSide.RIGHT ? 1 : -1;
        matrixStack.translate((float) i * 0.56F, -0.52F + equipProgress * -0.6F, -0.72F);
    }

    public void renderHandsWithItems(float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer.Impl buffers, @NotNull ClientPlayerEntity player, int light) {
        float f = player.getAttackAnim(partialTicks);
        Hand hand = MoreObjects.firstNonNull(player.swingingArm, Hand.MAIN_HAND);
        float f1 = MathHelper.lerp(partialTicks, player.xRotO, player.xRot);
        boolean flag = true;
        boolean flag1 = true;
        if (player.isUsingItem()) {
            ItemStack itemstack = player.getUseItem();
            if (itemstack.getItem() instanceof net.minecraft.item.ShootableItem) {
                flag = player.getUsedItemHand() == Hand.MAIN_HAND;
                flag1 = !flag;
            }

            Hand hand1 = player.getUsedItemHand();
            if (hand1 == Hand.MAIN_HAND) {
                ItemStack itemstack1 = player.getOffhandItem();
                if (itemstack1.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack1)) {
                    flag1 = false;
                }
            }
        } else {
            ItemStack itemstack2 = player.getMainHandItem();
            ItemStack itemstack3 = player.getOffhandItem();
            if (itemstack2.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack2)) {
                flag1 = false;
            }

            if (itemstack3.getItem() == Items.CROSSBOW && CrossbowItem.isCharged(itemstack3)) {
                flag = !itemstack2.isEmpty();
                flag1 = !flag;
            }
        }

        float f3 = MathHelper.lerp(partialTicks, player.xBobO, player.xBob);
        float f4 = MathHelper.lerp(partialTicks, player.yBobO, player.yBob);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees((player.getViewXRot(partialTicks) - f3) * 0.1F));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees((player.getViewYRot(partialTicks) - f4) * 0.1F));
        if (flag) {
            float f5 = hand == Hand.MAIN_HAND ? f : 0.0F;
            float f2 = 1.0F - MathHelper.lerp(partialTicks, this.oMainHandHeight, this.mainHandHeight);
            this.renderArmWithItem(player, partialTicks, f1, Hand.MAIN_HAND, f5, this.mainHandItem, f2, matrixStack, buffers, light);
        }

        if (flag1) {
            float f6 = hand == Hand.OFF_HAND ? f : 0.0F;
            float f7 = 1.0F - MathHelper.lerp(partialTicks, this.oOffHandHeight, this.offHandHeight);
            this.renderArmWithItem(player, partialTicks, f1, Hand.OFF_HAND, f6, this.offHandItem, f7, matrixStack, buffers, light);
        }

        buffers.endBatch();
    }

    private void renderArmWithItem(AbstractClientPlayerEntity player, float partialTicks, float interpPitch, Hand hand, float swingProgress, @NotNull ItemStack stack, float equipProgress, @NotNull MatrixStack matrixStack, IRenderTypeBuffer buffers, int light) {
        boolean flag = hand == Hand.MAIN_HAND;
        HandSide handside = flag ? player.getMainArm() : player.getMainArm().getOpposite();
        matrixStack.pushPose();
        if (stack.isEmpty()) {
            if (flag && !player.isInvisible()) {
                this.renderPlayerArm(matrixStack, buffers, light, equipProgress, swingProgress, handside);
            }
        } else if (stack.getItem() == Items.FILLED_MAP) {
            if (flag && this.offHandItem.isEmpty()) {
                this.renderTwoHandedMap(matrixStack, buffers, light, interpPitch, equipProgress, swingProgress);
            } else {
                this.renderOneHandedMap(matrixStack, buffers, light, equipProgress, handside, swingProgress, stack);
            }
        } else if (stack.getItem() == Items.CROSSBOW) {
            boolean flag1 = CrossbowItem.isCharged(stack);
            boolean flag2 = handside == HandSide.RIGHT;
            int i = flag2 ? 1 : -1;
            if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand) {
                this.applyItemArmTransform(matrixStack, handside, equipProgress);
                matrixStack.translate((float) i * -0.4785682F, -0.094387F, 0.05731531F);
                matrixStack.mulPose(Vector3f.XP.rotationDegrees(-11.935F));
                matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) i * 65.3F));
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float) i * -9.785F));
                float f9 = (float) stack.getUseDuration() - ((float) player.getUseItemRemainingTicks() - partialTicks + 1.0F);
                float f13 = f9 / (float) CrossbowItem.getChargeDuration(stack);
                if (f13 > 1.0F) {
                    f13 = 1.0F;
                }

                if (f13 > 0.1F) {
                    float f16 = MathHelper.sin((f9 - 0.1F) * 1.3F);
                    float f3 = f13 - 0.1F;
                    float f4 = f16 * f3;
                    matrixStack.translate(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
                }

                matrixStack.translate(f13 * 0.0F, f13 * 0.0F, f13 * 0.04F);
                matrixStack.scale(1.0F, 1.0F, 1.0F + f13 * 0.2F);
                matrixStack.mulPose(Vector3f.YN.rotationDegrees((float) i * 45.0F));
            } else {
                float f = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
                float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * ((float) Math.PI * 2F));
                float f2 = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
                matrixStack.translate((float) i * f, f1, f2);
                this.applyItemArmTransform(matrixStack, handside, equipProgress);
                this.applyItemArmAttackTransform(matrixStack, handside, swingProgress);
                if (flag1 && swingProgress < 0.001F) {
                    matrixStack.translate((float) i * -0.641864F, 0.0D, 0.0D);
                    matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) i * 10.0F));
                }
            }

            this.renderItem(player, stack, flag2 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag2, matrixStack, buffers, light);
        } else {
            boolean flag3 = handside == HandSide.RIGHT;
            if (player.isUsingItem() && player.getUseItemRemainingTicks() > 0 && player.getUsedItemHand() == hand) {
                int k = flag3 ? 1 : -1;
                switch(stack.getUseAnimation()) {
                    case NONE:
                    case BLOCK:
                        this.applyItemArmTransform(matrixStack, handside, equipProgress);
                        break;
                    case EAT:
                    case DRINK:
                        this.applyEatTransform(matrixStack, partialTicks, handside, stack);
                        this.applyItemArmTransform(matrixStack, handside, equipProgress);
                        break;
                    case BOW:
                        this.applyItemArmTransform(matrixStack, handside, equipProgress);
                        matrixStack.translate((float) k * -0.2785682F, 0.18344387F, 0.15731531F);
                        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-13.935F));
                        matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) k * 35.3F));
                        matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float) k * -9.785F));
                        float f8 = (float) stack.getUseDuration() - ((float) player.getUseItemRemainingTicks() - partialTicks + 1.0F);
                        float f12 = f8 / 20.0F;
                        f12 = (f12 * f12 + f12 * 2.0F) / 3.0F;
                        if (f12 > 1.0F) {
                            f12 = 1.0F;
                        }

                        if (f12 > 0.1F) {
                            float f15 = MathHelper.sin((f8 - 0.1F) * 1.3F);
                            float f18 = f12 - 0.1F;
                            float f20 = f15 * f18;
                            matrixStack.translate(f20 * 0.0F, f20 * 0.004F, f20 * 0.0F);
                        }

                        matrixStack.translate(f12 * 0.0F, f12 * 0.0F, f12 * 0.04F);
                        matrixStack.scale(1.0F, 1.0F, 1.0F + f12 * 0.2F);
                        matrixStack.mulPose(Vector3f.YN.rotationDegrees((float) k * 45.0F));
                        break;
                    case SPEAR:
                        this.applyItemArmTransform(matrixStack, handside, equipProgress);
                        matrixStack.translate((float) k * -0.5F, 0.7F, 0.1F);
                        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-55.0F));
                        matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) k * 35.3F));
                        matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float) k * -9.785F));
                        float f7 = (float) stack.getUseDuration() - ((float) player.getUseItemRemainingTicks() - partialTicks + 1.0F);
                        float f11 = f7 / 10.0F;
                        if (f11 > 1.0F) {
                            f11 = 1.0F;
                        }

                        if (f11 > 0.1F) {
                            float f14 = MathHelper.sin((f7 - 0.1F) * 1.3F);
                            float f17 = f11 - 0.1F;
                            float f19 = f14 * f17;
                            matrixStack.translate(f19 * 0.0F, f19 * 0.004F, f19 * 0.0F);
                        }

                        matrixStack.translate(0.0D, 0.0D, f11 * 0.2F);
                        matrixStack.scale(1.0F, 1.0F, 1.0F + f11 * 0.2F);
                        matrixStack.mulPose(Vector3f.YN.rotationDegrees((float) k * 45.0F));
                }
            } else if (player.isAutoSpinAttack()) {
                this.applyItemArmTransform(matrixStack, handside, equipProgress);
                int j = flag3 ? 1 : -1;
                matrixStack.translate((float) j * -0.4F, 0.8F, 0.3F);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees((float) j * 65.0F));
                matrixStack.mulPose(Vector3f.ZP.rotationDegrees((float) j * -85.0F));
            } else {
                float f5 = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
                float f6 = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * ((float) Math.PI * 2F));
                float f10 = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
                int l = flag3 ? 1 : -1;
                matrixStack.translate((float) l * f5, f6, f10);
                this.applyItemArmTransform(matrixStack, handside, equipProgress);
                this.applyItemArmAttackTransform(matrixStack, handside, swingProgress);
            }

            this.renderItem(player, stack, flag3 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag3, matrixStack, buffers, light);
        }

        matrixStack.popPose();
    }

    public void tick() {
        this.oMainHandHeight = this.mainHandHeight;
        this.oOffHandHeight = this.offHandHeight;
        ClientPlayerEntity player = this.minecraft.player;
        if (player != null) {
            ModPlayerInventory inventory = ModPlayerInventory.get(player);
            ItemStack itemstack = inventory.status ? inventory.getSelected() : player.getMainHandItem();
            ItemStack itemstack1 = player.getOffhandItem();
            if (ItemStack.matches(this.mainHandItem, itemstack)) {
                this.mainHandItem = itemstack;
            }

            if (ItemStack.matches(this.offHandItem, itemstack1)) {
                this.offHandItem = itemstack1;
            }

            if (player.isHandsBusy()) {
                this.mainHandHeight = MathHelper.clamp(this.mainHandHeight - 0.4F, 0.0F, 1.0F);
                this.offHandHeight = MathHelper.clamp(this.offHandHeight - 0.4F, 0.0F, 1.0F);
            } else {
                float f = player.getAttackStrengthScale(1.0F);
                boolean requipM = net.minecraftforge.client.ForgeHooksClient.shouldCauseReequipAnimation(this.mainHandItem, itemstack, inventory.status ? inventory.selected + 41 : player.inventory.selected);
                boolean requipO = net.minecraftforge.client.ForgeHooksClient.shouldCauseReequipAnimation(this.offHandItem, itemstack1, -1);

                if (!requipM && this.mainHandItem != itemstack)
                    this.mainHandItem = itemstack;
                if (!requipO && this.offHandItem != itemstack1)
                    this.offHandItem = itemstack1;

                this.mainHandHeight += MathHelper.clamp((!requipM ? f * f * f : 0.0F) - this.mainHandHeight, -0.4F, 0.4F);
                this.offHandHeight += MathHelper.clamp((float) (!requipO ? 1 : 0) - this.offHandHeight, -0.4F, 0.4F);
            }

            if (this.mainHandHeight < 0.1F) {
                this.mainHandItem = itemstack;
            }

            if (this.offHandHeight < 0.1F) {
                this.offHandItem = itemstack1;
            }
        }
    }
}
