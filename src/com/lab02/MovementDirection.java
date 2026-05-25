package com.lab02;

// Направление перемещения для стратегий
public enum MovementDirection {
    UP(1),
    DOWN(2),
    LEFT(3),
    RIGHT(4);

    private final int value;

    MovementDirection(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // Преобразование числа в перечисление
    public static MovementDirection fromInt(int value) {
        for (MovementDirection dir : values()) {
            if (dir.value == value) {
                return dir;
            }
        }
        return UP;
    }
}