package com.lab02;

import java.awt.*;

public class DrawingBoat {
    protected EntityBoat entityBoat;
    protected Integer startPosX;
    protected Integer startPosY;

    protected int boatWidth = 100;
    protected int boatHeight = 40;

    protected IOarDrawer oarDrawer;

    public DrawingBoat(int speed, double weight, Color bodyColor) {
        entityBoat = new EntityBoat();
        entityBoat.init(speed, weight, bodyColor);
        startPosX = null;
        startPosY = null;
        oarDrawer = null;
    }

    protected DrawingBoat(int boatWidth, int boatHeight) {
        this.boatWidth = boatWidth;
        this.boatHeight = boatHeight;
        startPosX = null;
        startPosY = null;
    }

    public void setOarDrawer(IOarDrawer oarDrawer) {
        this.oarDrawer = oarDrawer;
    }

    public void setPosition(int x, int y) {
        this.startPosX = x;
        this.startPosY = y;
    }

    public Integer getPosX() { return startPosX; }
    public Integer getPosY() { return startPosY; }
    public int getBoatWidth() { return boatWidth; }
    public int getBoatHeight() { return boatHeight; }
    public Double getBoatStep() { return entityBoat != null ? entityBoat.getStep() : null; }

    public void moveLeft() {
        if (entityBoat == null || startPosX == null) return;
        startPosX -= (int) entityBoat.getStep();
    }

    public void moveRight() {
        if (entityBoat == null || startPosX == null) return;
        startPosX += (int) entityBoat.getStep();
    }

    public void moveUp() {
        if (entityBoat == null || startPosY == null) return;
        startPosY -= (int) entityBoat.getStep();
    }

    public void moveDown() {
        if (entityBoat == null || startPosY == null) return;
        startPosY += (int) entityBoat.getStep();
    }

    public void drawTransport(Graphics g) {
        if (entityBoat == null || startPosX == null || startPosY == null) return;

        int x = startPosX;
        int y = startPosY;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));

        // Корпус лодки
        int[] boatXPoints = {x, x + 70, x + boatWidth, x + 70, x};
        int[] boatYPoints = {y, y, y + 20, y + 40, y + 40};
        g2d.setColor(entityBoat.getBodyColor());
        g2d.fillPolygon(boatXPoints, boatYPoints, 5);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(boatXPoints, boatYPoints, 5);

        // Внутренний закруглённый элемент (каюта)
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRoundRect(x + 8, y + 6, 55, 28, 10, 10);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(x + 8, y + 6, 55, 28, 10, 10);

        // Вёсла (если есть)
        if (oarDrawer != null) {
            oarDrawer.draw(g, x, y, boatWidth, boatHeight, new Color(139, 69, 19));
        }
    }
}