package com.lab03;

import java.awt.*;

public class EntityBoat {
    private int speed;
    private double weight;
    private Color bodyColor;

    public void init(int speed, double weight, Color bodyColor) {
        this.speed = speed;
        this.weight = weight;
        this.bodyColor = bodyColor;
    }

    public int getSpeed() { return speed; }
    public double getWeight() { return weight; }
    public Color getBodyColor() { return bodyColor; }

    public double getStep() {
        double step = speed * 100.0 / weight;
        return step < 5 ? 5 : step;
    }
}