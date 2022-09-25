package com.mattzm.gungale.util.color;

import net.minecraft.util.math.vector.Vector3i;
import org.jetbrains.annotations.NotNull;

public enum HitPartColor {
    HEAD_SHIELD, BODY_SHIELD, HEAD_NAKED, BODY_NAKED;

    public Vector3i @NotNull [] getRGB() {
        if (this == HEAD_SHIELD) {
            return new Vector3i[]{new Vector3i(255, 245, 0), new Vector3i(170, 80, 60)};
        } else if (this == BODY_SHIELD) {
            return new Vector3i[]{new Vector3i(255, 255, 255), new Vector3i(180, 125, 185)};
        } else if (this == HEAD_NAKED) {
            return new Vector3i[]{new Vector3i(255, 235, 5), new Vector3i(205, 100, 60)};
        } else {
            return new Vector3i[]{new Vector3i(255, 250, 250), new Vector3i(205, 15, 15)};
        }
    }
}
