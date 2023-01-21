package com.mattzm.gungale.item.weapon;

import com.mattzm.gungale.message.DamageType;
import com.mattzm.gungale.message.FireTarget;
import com.mattzm.gungale.potion.ModEffects;
import com.mattzm.gungale.property.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractEnergyWeaponItem extends AbstractAutoWeaponItem {
    protected AbstractEnergyWeaponItem(@NotNull BasicProperty basicProperty, @NotNull DamageProperty damageProperty, @NotNull RecoilProperty recoilProperty, @NotNull ReloadProperty reloadProperty, @NotNull ADSProperty adsProperty, @NotNull MagProperty magProperty, int mobility) {
        super(basicProperty, damageProperty, recoilProperty, reloadProperty, adsProperty, magProperty, mobility);
    }

    @Override
    public void hurt(@NotNull LivingEntity entity, PlayerEntity player, float damage) {
        super.hurt(entity, player, damage);
        entity.addEffect(new EffectInstance(ModEffects.ENERGY_BURNT, 40, 0, false, false));
    }

    @Override
    protected FireTarget analysis(BlockRayTraceResult blockResult, EntityRayTraceResult entityResult, Vector3d eyePosition, double blockDis) {
        FireTarget superTarget = super.analysis(blockResult, entityResult, eyePosition, blockDis);
        if (superTarget == FireTarget.ENTITY) {
            return superTarget.setDamageAmplifier(1.0f);
        } else if (entityResult == null || !(entityResult.getEntity() instanceof LivingEntity)) {
            return FireTarget.BLOCK.setPosition(blockResult.getLocation());
        } else {
            double entityDis = eyePosition.distanceToSqr(entityResult.getLocation());
            if (Math.sqrt(entityDis) < Math.sqrt(blockDis) + 2.0) {
                World world = entityResult.getEntity().level;
                BlockState blockState = world.getBlockState(blockResult.getBlockPos());
                float resistance = blockState.getBlock().getExplosionResistance(blockState, world, blockResult.getBlockPos(), null);
                float amplifier = resistance >= 6.0f ? 0.5f * (6.0f / resistance) : resistance == 0.0f ? 1.0f : 1 - 0.5f / (6.0f / resistance);
                amplifier = amplifier >= 0.05f ? amplifier : 0.0f;
                if (entityResult.getEntity() instanceof PlayerEntity) {
                    return FireTarget.ENTITY.setEntity((LivingEntity) entityResult.getEntity()).setDamageType(DamageType.getHitPart(entityResult)).setDamageAmplifier(amplifier);
                } else {
                    return FireTarget.ENTITY.setEntity((LivingEntity) entityResult.getEntity()).setDamageType(DamageType.BODY).setDamageAmplifier(amplifier);
                }
            } else {
                return FireTarget.BLOCK.setPosition(blockResult.getLocation());
            }
        }
    }
}
