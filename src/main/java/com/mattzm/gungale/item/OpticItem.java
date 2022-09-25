package com.mattzm.gungale.item;

import com.mattzm.gungale.util.math.ModMathHelper;

public class OpticItem extends AttachmentItem {
    public static final double[] MAGNIFICATION_FOV = {60.0, ModMathHelper.getFovWithMagnification(70, 1), ModMathHelper.getFovWithMagnification(70, 2), ModMathHelper.getFovWithMagnification(70, 3), ModMathHelper.getFovWithMagnification(70, 4)};

    public final int magnification;

    public OpticItem(int magnification) {
        this.magnification = magnification;
    }
}
