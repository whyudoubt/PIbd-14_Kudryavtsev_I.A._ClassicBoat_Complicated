package com.lab05;

import java.awt.*;

public class DrawingImprovedBoat extends DrawingBoat {
    private Color sailColor;
    private boolean hasSail;

    public DrawingImprovedBoat(int speed, double weight, Color bodyColor,
                               Color sailColor, boolean hasSail) {
        super(110, 40);
        EntityImprovedBoat improvedBoat = new EntityImprovedBoat();
        improvedBoat.init(speed, weight, bodyColor, sailColor, hasSail);
        this.entityBoat = improvedBoat;
        this.sailColor = sailColor;
        this.hasSail = hasSail;
        this.startPosX = null;
        this.startPosY = null;
        this.oarDrawer = null;
    }

    public void updateHasSail(boolean hasSail) {
        this.hasSail = hasSail;
        if (entityBoat instanceof EntityImprovedBoat improved) {
            improved.updateHasSail(hasSail);
        }
    }

    public void changeAdditionalColor(Color newColor) {
        this.sailColor = newColor;
        if (entityBoat instanceof EntityImprovedBoat improved) {
            improved.changeAdditionalColor(newColor);
        }
    }

    public Color getSailColor() {
        return sailColor;
    }

    public boolean hasSail() {
        return hasSail;
    }

    @Override
    public void drawTransport(Graphics g) {
        if (entityBoat == null || startPosX == null || startPosY == null) return;

        int x = startPosX;
        int y = startPosY;

        // Рисуем базовую часть
        super.drawTransport(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));

        // Парус
        if (hasSail) {
            int[] sailX = {x + 50, x + 75, x + 50, x + 25};
            int[] sailY = {y + 5, y + 20, y + 35, y + 20};
            g2d.setColor(sailColor);
            g2d.fillPolygon(sailX, sailY, 4);
            g2d.setColor(Color.BLACK);
            g2d.drawPolygon(sailX, sailY, 4);
            g2d.drawLine(x + 50, y + 5, x + 50, y + 35);
            g2d.drawLine(x + 25, y + 20, x + 75, y + 20);
            g2d.fillOval(x + 47, y + 17, 6, 6);
        }
    }
}