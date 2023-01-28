package com.mattzm.gungale.item;

public class HopUpItem extends AttachmentItem {
    private final Type type;

    public HopUpItem(Type type) {
        this.type = type;
    }

    public enum Type {
        TURBOCHARGER
    }
}
