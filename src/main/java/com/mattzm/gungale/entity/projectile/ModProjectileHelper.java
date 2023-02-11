package com.mattzm.gungale.entity.projectile;

import com.mattzm.gungale.client.object.ClientObjectHolder;
import com.mattzm.gungale.item.GearItem;
import com.mattzm.gungale.message.DamageType;
import com.mattzm.gungale.nbt.stack.ShieldDamageNBT;
import com.mattzm.gungale.util.color.ColoredText;
import com.mattzm.gungale.util.color.HitPartColor;
import com.mattzm.gungale.util.math.ModMathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ModProjectileHelper {
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public static EntityRayTraceResult getEntityHitResult(@NotNull PlayerEntity player, Vector3d eyePos, Vector3d pos, AxisAlignedBB bb, double maxDis) {
        World world = player.level;
        Entity target = null;
        Vector3d targetPos = null;

        for (Entity entity : world.getEntities(player, bb, entity -> !entity.isSpectator())) {
            AxisAlignedBB entityBB = entity.getBoundingBox();
            if (entity instanceof PlayerEntity) {
                entityBB.expandTowards(0.0, 0.2, 0.0);
            }

            Optional<Vector3d> optional = entityBB.clip(eyePos, pos);
            if (entityBB.contains(eyePos)) {
                if (maxDis >= 0.0D) {
                    target = entity;
                    targetPos = optional.orElse(eyePos);
                    maxDis = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vector3d vector3d = optional.get();
                double d1 = entity.distanceToSqr(vector3d);
                if (d1 < maxDis || maxDis == 0.0D) {
                    if (entity.getRootVehicle() == player.getRootVehicle() && !entity.canRiderInteract()) {
                        if (maxDis == 0.0D) {
                            target = entity;
                            targetPos = vector3d;
                        }
                    } else {
                        target = entity;
                        targetPos = vector3d;
                        maxDis = d1;
                    }
                }
            }
        }

        return target == null ? null : new EntityRayTraceResult(target, targetPos);
    }

    public static @NotNull ColoredText getTextByDamage(float damage, LivingEntity target, DamageType damageType) {
        damage = ModMathHelper.oneDigitFloat(damage);
        String text = Float.toString(damage);
        Vector3i[] colors;
        if (damageType == DamageType.HEAD) {
            if (GearItem.hasGear(target) && target instanceof PlayerEntity) {
                if (ShieldDamageNBT.getResistance(GearItem.get((PlayerEntity) target)) > 0) {
                    colors = HitPartColor.HEAD_SHIELD.getRGB();
                } else {
                    colors = HitPartColor.HEAD_NAKED.getRGB();
                }
            } else {
                colors = HitPartColor.HEAD_NAKED.getRGB();
            }
        } else if (GearItem.hasGear(target) && target instanceof PlayerEntity) {
            if (ShieldDamageNBT.getResistance(GearItem.get((PlayerEntity) target)) > 0) {
                colors = HitPartColor.BODY_SHIELD.getRGB();
            } else {
                colors = HitPartColor.BODY_NAKED.getRGB();
            }
        } else {
            colors = HitPartColor.BODY_NAKED.getRGB();
        }

        for (int i = ClientObjectHolder.getInstance().getMIngameGui().getDamageTexts().length - 1; i >= 0; i--) {
            ColoredText[] damageTexts = ClientObjectHolder.getInstance().getMIngameGui().getDamageTexts();
            Vector2f[] damagePoses = ClientObjectHolder.getInstance().getMIngameGui().getDamagePoses();
            if (i < damageTexts.length - 1 && damageTexts[i] != null) {
                damageTexts[i + 1] = damageTexts[i].baseOn(0.6f, i * 10.0f + 35.0f);
                damagePoses[i + 1] = new Vector2f(damagePoses[i].x, damagePoses[i].y);
            }
        }

        return new ColoredText(text, colors[0], colors[1], 1.0f, 25.0f);
    }
}
