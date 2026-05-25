package com.lab01;

import java.awt.*;
import java.util.Random;

public class DrawingBoat {
    private EntityBoat entityBoat;
    private Integer startPosX;
    private Integer startPosY;

    private final int boatWidth = 100;
    private final int boatHeight = 40;

    // Поле от класса КлассДоп (вёсла)
    private Oar oar;

    public DrawingBoat() {
        this.entityBoat = null;
        this.startPosX = null;
        this.startPosY = null;

        // Инициализация вёсел со случайным количеством (1, 2 или 3)
        Random random = new Random();
        int oarCountValue = random.nextInt(3) + 1; // 1, 2 или 3
        this.oar = new Oar(OarCount.fromInt(oarCountValue));
    }

    public void init(int speed, double weight, Color bodyColor) {
        entityBoat = new EntityBoat();
        entityBoat.init(speed, weight, bodyColor);
        startPosX = null;
        startPosY = null;
    }

    public void setPosition(int x, int y) {
        this.startPosX = x;
        this.startPosY = y;
    }

    public Integer getPosX() {
        return startPosX;
    }

    public Integer getPosY() {
        return startPosY;
    }

    public int getBoatWidth() {
        return boatWidth;
    }

    public int getBoatHeight() {
        return boatHeight;
    }

    public Double getBoatStep() {
        return entityBoat != null ? entityBoat.getStep() : null;
    }

    // Метод для изменения количества вёсел (числовое свойство)
    public void setOarCount(int count) {
        if (oar != null) {
            oar.setOarCount(count);
        }
    }

    public int getOarCount() {
        return oar != null ? oar.getOarCount().getValue() : 2;
    }

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

    // Прорисовка лодки + вызов прорисовки вёсел
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

        // Рисуем вёсла (вызов метода прорисовки класса КлассДоп)
        if (oar != null) {
            oar.draw(g, x, y, boatWidth, boatHeight, new Color(139, 69, 19)); // коричневый цвет вёсел
        }
    }
}