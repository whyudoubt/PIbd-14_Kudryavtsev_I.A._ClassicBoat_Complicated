package com.lab03;

public enum OarCount {
    ONE(1),
    TWO(2),
    THREE(3);

    private final int value;

    OarCount(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static OarCount fromInt(int value) {
        for (OarCount count : values()) {
            if (count.value == value) {
                return count;
            }
        }
        return TWO;
    }
}