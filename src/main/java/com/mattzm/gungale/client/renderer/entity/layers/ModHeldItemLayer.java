package com.mattzm.gungale.client.renderer.entity.layers;

import com.mattzm.gungale.entity.player.ModPlayerInventory;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;
import org.jetbrains.annotations.NotNull;

public class ModHeldItemLayer<T extends LivingEntity, M extends EntityModel<T> & IHasArm> extends HeldItemLayer<T, M> {
    public ModHeldItemLayer(IEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NotNull MatrixStack matrixStack, @NotNull IRenderTypeBuffer buffers, int light, @NotNull T entity, float p_225628_5_, float p_225628_6_, float p_225628_7_, float p_225628_8_, float p_225628_9_, float p_225628_10_) {
        boolean flag = entity.getMainArm() == HandSide.RIGHT;
        if (Minecraft.getInstance().player != null) {
            ModPlayerInventory inventory = ModPlayerInventory.get(Minecraft.getInstance().player);
            ItemStack leftHandItem = flag ? entity.getOffhandItem() : inventory.status ? inventory.getSelected() : entity.getMainHandItem();
            ItemStack rightHandItem = flag ? inventory.status ? inventory.getSelected() : entity.getMainHandItem() : entity.getOffhandItem();
            if (!leftHandItem.isEmpty() || !rightHandItem.isEmpty()) {
                matrixStack.pushPose();
                if (this.getParentModel().young) {
                    matrixStack.translate(0.0D, 0.75D, 0.0D);
                    matrixStack.scale(0.5F, 0.5F, 0.5F);
                }

                this.renderArmWithItem(entity, rightHandItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrixStack, buffers, light);
                this.renderArmWithItem(entity, leftHandItem, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrixStack, buffers, light);
                matrixStack.popPose();
            }
        }
    }

    private void renderArmWithItem(LivingEntity entity, @NotNull ItemStack stack, ItemCameraTransforms.TransformType transformType, HandSide handSide, MatrixStack matrixStack, IRenderTypeBuffer buffers, int light) {
        if (!stack.isEmpty()) {
            matrixStack.pushPose();
            this.getParentModel().translateToHand(handSide, matrixStack);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            boolean flag = handSide == HandSide.LEFT;
            matrixStack.translate((float) (flag ? -1 : 1) / 16.0F, 0.125D, -0.625D);
            Minecraft.getInstance().getItemInHandRenderer().renderItem(entity, stack, transformType, flag, matrixStack, buffers, light);
            matrixStack.popPose();
        }
    }
}
