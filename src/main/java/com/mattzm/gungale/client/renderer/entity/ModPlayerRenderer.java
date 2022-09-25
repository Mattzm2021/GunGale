package com.mattzm.gungale.client.renderer.entity;

import com.mattzm.gungale.client.renderer.entity.layers.ModHeldItemLayer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderNameplateEvent;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class ModPlayerRenderer extends LivingRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
    private PlayerModel<AbstractClientPlayerEntity> model;

    public ModPlayerRenderer(EntityRendererManager dispatcher) {
        super(dispatcher, new PlayerModel<>(0.0F, false), 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, new BipedModel<>(0.5F), new BipedModel<>(1.0F)));
        this.addLayer(new ModHeldItemLayer<>(this));
        this.addLayer(new ArrowLayer<>(this));
        this.addLayer(new Deadmau5HeadLayer(this));
        this.addLayer(new CapeLayer(this));
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new ElytraLayer<>(this));
        this.addLayer(new ParrotVariantLayer<>(this));
        this.addLayer(new SpinAttackEffectLayer<>(this));
        this.addLayer(new BeeStingerLayer<>(this));
    }

    @Override
    public @NotNull PlayerModel<AbstractClientPlayerEntity> getModel() {
        return this.model;
    }

    public void render(@NotNull AbstractClientPlayerEntity player, float partialTicks, @NotNull MatrixStack matrixStack, IRenderTypeBuffer buffers, int light) {
        matrixStack.pushPose();
        this.model.attackTime = this.getAttackAnim(player, partialTicks);
        boolean shouldSit = player.isPassenger() && (player.getVehicle() != null && player.getVehicle().shouldRiderSit());
        this.model.riding = shouldSit;
        this.model.young = player.isBaby();
        float f = MathHelper.rotLerp(partialTicks, player.yBodyRotO, player.yBodyRot);
        float f1 = MathHelper.rotLerp(partialTicks, player.yHeadRotO, player.yHeadRot);
        float f2 = f1 - f;
        if (shouldSit && player.getVehicle() instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)player.getVehicle();
            f = MathHelper.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
            f2 = f1 - f;
            float f3 = MathHelper.wrapDegrees(f2);
            if (f3 < -85.0F) {
                f3 = -85.0F;
            }

            if (f3 >= 85.0F) {
                f3 = 85.0F;
            }

            f = f1 - f3;
            if (f3 * f3 > 2500.0F) {
                f += f3 * 0.2F;
            }

            f2 = f1 - f;
        }

        float f6 = MathHelper.lerp(partialTicks, player.xRotO, player.xRot);
        if (player.getPose() == Pose.SLEEPING) {
            Direction direction = player.getBedOrientation();
            if (direction != null) {
                float f4 = player.getEyeHeight(Pose.STANDING) - 0.1F;
                matrixStack.translate((float) (-direction.getStepX()) * f4, 0.0D, (float) (-direction.getStepZ()) * f4);
            }
        }

        float f7 = this.getBob(player, partialTicks);
        this.setupRotations(player, matrixStack, f7, f, partialTicks);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(player, matrixStack, partialTicks);
        matrixStack.translate(0.0D, -1.501F, 0.0D);
        float f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && player.isAlive()) {
            f8 = MathHelper.lerp(partialTicks, player.animationSpeedOld, player.animationSpeed);
            f5 = player.animationPosition - player.animationSpeed * (1.0F - partialTicks);
            if (player.isBaby()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        this.model.prepareMobModel(player, f5, f8, partialTicks);
        this.model.setupAnim(player, f5, f8, f7, f2, f6);
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = this.isBodyVisible(player);
        boolean flag1 = !flag && minecraft.player != null && !player.isInvisibleTo(minecraft.player);
        boolean flag2 = minecraft.shouldEntityAppearGlowing(player);
        RenderType rendertype = this.getRenderType(player, flag, flag1, flag2);
        if (rendertype != null) {
            IVertexBuilder ivertexbuilder = buffers.getBuffer(rendertype);
            int i = getOverlayCoords(player, this.getWhiteOverlayProgress(player, partialTicks));
            this.model.renderToBuffer(matrixStack, ivertexbuilder, light, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
        }

        if (!player.isSpectator()) {
            for (LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> layerrenderer : this.layers) {
                layerrenderer.render(matrixStack, buffers, light, player, f5, f8, partialTicks, f7, f2, f6);
            }
        }

        matrixStack.popPose();
        RenderNameplateEvent renderNameplateEvent = new RenderNameplateEvent(player, player.getDisplayName(), this, matrixStack, buffers, light, partialTicks);
        MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
        if (renderNameplateEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameplateEvent.getResult() == net.minecraftforge.eventbus.api.Event.Result.ALLOW || this.shouldShowName(player))) {
            this.renderNameTag(player, renderNameplateEvent.getContent(), matrixStack, buffers, light);
        }
    }

    @Override
    @Nullable
    protected RenderType getRenderType(@NotNull AbstractClientPlayerEntity player, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        ResourceLocation resourcelocation = this.getTextureLocation(player);
        if (p_230496_3_) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (p_230496_2_) {
            return this.model.renderType(resourcelocation);
        } else {
            return p_230496_4_ ? RenderType.outline(resourcelocation) : null;
        }
    }

    @Override
    public @NotNull Vector3d getRenderOffset(@NotNull AbstractClientPlayerEntity p_225627_1_, float p_225627_2_) {
        return p_225627_1_.isCrouching() ? new Vector3d(0.0D, -0.125D, 0.0D) : super.getRenderOffset(p_225627_1_, p_225627_2_);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull AbstractClientPlayerEntity player) {
        return player.getSkinTextureLocation();
    }

    @Override
    protected void scale(@NotNull AbstractClientPlayerEntity player, @NotNull MatrixStack matrixStack, float p_225620_3_) {
        matrixStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    protected void renderNameTag(@NotNull AbstractClientPlayerEntity player, @NotNull ITextComponent text, @NotNull MatrixStack matrixStack, @NotNull IRenderTypeBuffer buffers, int light) {
        double d0 = this.entityRenderDispatcher.distanceToSqr(player);
        matrixStack.pushPose();
        if (d0 < 100.0D) {
            Scoreboard scoreboard = player.getScoreboard();
            ScoreObjective scoreobjective = scoreboard.getDisplayObjective(2);
            if (scoreobjective != null) {
                Score score = scoreboard.getOrCreatePlayerScore(player.getScoreboardName(), scoreobjective);
                super.renderNameTag(player, (new StringTextComponent(Integer.toString(score.getScore()))).append(" ").append(scoreobjective.getDisplayName()), matrixStack, buffers, light);
                matrixStack.translate(0.0D, 9.0F * 1.15F * 0.025F, 0.0D);
            }
        }

        super.renderNameTag(player, text, matrixStack, buffers, light);
        matrixStack.popPose();
    }

    @Override
    protected void setupRotations(@NotNull AbstractClientPlayerEntity p_225621_1_, @NotNull MatrixStack p_225621_2_, float p_225621_3_, float p_225621_4_, float p_225621_5_) {
        float f = p_225621_1_.getSwimAmount(p_225621_5_);
        if (p_225621_1_.isFallFlying()) {
            super.setupRotations(p_225621_1_, p_225621_2_, p_225621_3_, p_225621_4_, p_225621_5_);
            float f1 = (float) p_225621_1_.getFallFlyingTicks() + p_225621_5_;
            float f2 = MathHelper.clamp(f1 * f1 / 100.0F, 0.0F, 1.0F);
            if (!p_225621_1_.isAutoSpinAttack()) {
                p_225621_2_.mulPose(Vector3f.XP.rotationDegrees(f2 * (-90.0F - p_225621_1_.xRot)));
            }

            Vector3d vector3d = p_225621_1_.getViewVector(p_225621_5_);
            Vector3d vector3d1 = p_225621_1_.getDeltaMovement();
            double d0 = Entity.getHorizontalDistanceSqr(vector3d1);
            double d1 = Entity.getHorizontalDistanceSqr(vector3d);
            if (d0 > 0.0D && d1 > 0.0D) {
                double d2 = (vector3d1.x * vector3d.x + vector3d1.z * vector3d.z) / Math.sqrt(d0 * d1);
                double d3 = vector3d1.x * vector3d.z - vector3d1.z * vector3d.x;
                p_225621_2_.mulPose(Vector3f.YP.rotation((float)(Math.signum(d3) * Math.acos(d2))));
            }
        } else if (f > 0.0F) {
            super.setupRotations(p_225621_1_, p_225621_2_, p_225621_3_, p_225621_4_, p_225621_5_);
            float f3 = p_225621_1_.isInWater() ? -90.0F - p_225621_1_.xRot : -90.0F;
            float f4 = MathHelper.lerp(f, 0.0F, f3);
            p_225621_2_.mulPose(Vector3f.XP.rotationDegrees(f4));
            if (p_225621_1_.isVisuallySwimming()) {
                p_225621_2_.translate(0.0D, -1.0D, 0.3F);
            }
        } else {
            super.setupRotations(p_225621_1_, p_225621_2_, p_225621_3_, p_225621_4_, p_225621_5_);
        }
    }

    public void setModel(@NotNull PlayerModel<AbstractClientPlayerEntity> model) {
        this.model = model;
    }
}