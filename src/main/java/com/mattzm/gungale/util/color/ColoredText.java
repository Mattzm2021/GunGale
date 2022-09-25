package com.mattzm.gungale.util.color;

import net.minecraft.util.math.vector.Vector3i;
import org.jetbrains.annotations.NotNull;

public class ColoredText {
    public final String text;
    public Vector3i inner;
    public Vector3i outer;
    public float size;
    public float margin;

    public ColoredText(String text, Vector3i inner, Vector3i outer, float size, float margin) {
        this.text = text;
        this.inner = inner;
        this.outer = outer;
        this.size = size;
        this.margin = margin;
    }

    public void baseOn(@NotNull ColoredText coloredText) {
        this.inner = coloredText.inner;
        this.outer = coloredText.outer;
        this.size = coloredText.size;
        this.margin = coloredText.margin;
    }

    public ColoredText baseOn(float size, float margin) {
        return new ColoredText(this.text, this.inner, this.outer, size, margin);
    }

    public static int toInt(@NotNull Vector3i vector3i) {
        return vector3i.getX() * 65536 + vector3i.getY() * 256 + vector3i.getZ();
    }
}
