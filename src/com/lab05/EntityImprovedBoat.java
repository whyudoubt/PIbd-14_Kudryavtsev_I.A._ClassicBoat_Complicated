package com.lab05;

import java.awt.*;

public class EntityImprovedBoat extends EntityBoat {
    private Color sailColor;
    private boolean hasSail;

    public void init(int speed, double weight, Color bodyColor, Color sailColor, boolean hasSail) {
        super.init(speed, weight, bodyColor);
        this.sailColor = sailColor;
        this.hasSail = hasSail;
    }

    public Color getSailColor() { return sailColor; }
    public boolean hasSail() { return hasSail; }

    public void updateHasSail(boolean hasSail) {
        this.hasSail = hasSail;
    }

    public void changeAdditionalColor(Color newColor) {
        this.sailColor = newColor;
    }
}