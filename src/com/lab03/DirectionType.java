package com.lab03;

public enum DirectionType {
    NONE(0),
    UP(1),
    DOWN(2),
    LEFT(3),
    RIGHT(4);

    private final int value;

    DirectionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DirectionType fromInt(int value) {
        for (DirectionType dir : values()) {
            if (dir.value == value) return dir;
        }
        return NONE;
    }
}