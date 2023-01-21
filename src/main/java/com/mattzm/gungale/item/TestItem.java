package com.mattzm.gungale.item;

import com.mattzm.gungale.entity.util.EntityHelper;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;

public class TestItem extends Item {
    private static final UUID SPEED_UUID = UUID.fromString("fc29ad6b-e58e-4a07-a73d-4500414f1eb2");

    public TestItem(Properties properties) {
        super(properties);
    }

    @ParametersAreNonnullByDefault
    @Override
    public @NotNull ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!EntityHelper.getSpeedAttributeInstance(player).hasModifier(getSpeedModifier(1.0))) {
            EntityHelper.getSpeedAttributeInstance(player).addTransientModifier(getSpeedModifier(0.3));
        }

        return ActionResult.pass(player.getMainHandItem());
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, PlayerEntity player) {
        if (EntityHelper.getSpeedAttributeInstance(player).hasModifier(getSpeedModifier(1.0))) {
            EntityHelper.getSpeedAttributeInstance(player).removeModifier(SPEED_UUID);
        }

        return false;
    }

    private static @NotNull AttributeModifier getSpeedModifier(double amount) {
        return new AttributeModifier(SPEED_UUID, "Speed", amount, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }
}
