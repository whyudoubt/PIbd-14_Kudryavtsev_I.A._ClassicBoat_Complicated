package com.lab03;

// Класс для хранения координат объекта
public class ObjectCoordinates {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public ObjectCoordinates(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    // Левая граница
    public int getLeftBorder() { return x; }

    // Верхняя граница
    public int getTopBorder() { return y; }

    // Правая граница
    public int getRightBorder() { return x + width; }

    // Нижняя граница
    public int getDownBorder() { return y + height; }

    // Середина объекта по горизонтали
    public int getObjectMiddleHorizontal() { return x + width / 2; }

    // Середина объекта по вертикали
    public int getObjectMiddleVertical() { return y + height / 2; }
}