package com.lab03;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CanvasForBoat {
    private DrawingBoat drawingBoat;
    private int canvasWidth;
    private int canvasHeight;
    private boolean sizeSet;

    public CanvasForBoat() {
        this.sizeSet = false;
    }

    public void setPictureSize(int width, int height) {
        this.canvasWidth = width;
        this.canvasHeight = height;
        this.sizeSet = true;
        System.out.println("CanvasForBoat: размеры поля установлены " + width + "x" + height);
    }

    public boolean insertBoat(DrawingBoat boat) {
        if (!sizeSet) {
            System.out.println("insertBoat: размеры поля не заданы");
            return false;
        }

        if (boat == null) {
            System.out.println("insertBoat: лодка null");
            return false;
        }

        if (boat.getBoatWidth() > canvasWidth || boat.getBoatHeight() > canvasHeight) {
            System.out.println("insertBoat: лодка слишком большая " + boat.getBoatWidth() + "x" + boat.getBoatHeight() +
                    ", поле " + canvasWidth + "x" + canvasHeight);
            return false;
        }

        this.drawingBoat = boat;
        System.out.println("insertBoat: лодка успешно вставлена");
        return true;
    }

    public void setBoatPosition(int x, int y) {
        if (!sizeSet || drawingBoat == null) return;

        int finalX = x;
        int finalY = y;

        if (finalX < 0) finalX = 0;
        if (finalX + drawingBoat.getBoatWidth() > canvasWidth) {
            finalX = canvasWidth - drawingBoat.getBoatWidth();
        }
        if (finalY < 0) finalY = 0;
        if (finalY + drawingBoat.getBoatHeight() > canvasHeight) {
            finalY = canvasHeight - drawingBoat.getBoatHeight();
        }

        drawingBoat.setPosition(finalX, finalY);
    }

    public boolean moveTransport(DirectionType direction) {
        if (!sizeSet || drawingBoat == null || drawingBoat.getPosX() == null ||
                drawingBoat.getPosY() == null || drawingBoat.getBoatStep() == null) {
            return false;
        }

        int step = (int) Math.round(drawingBoat.getBoatStep());
        if (step <= 0) step = 5;

        switch (direction) {
            case LEFT:
                if (drawingBoat.getPosX() - step >= 0) {
                    drawingBoat.moveLeft();
                    return true;
                }
                break;
            case UP:
                if (drawingBoat.getPosY() - step >= 0) {
                    drawingBoat.moveUp();
                    return true;
                }
                break;
            case RIGHT:
                if (drawingBoat.getPosX() + step + drawingBoat.getBoatWidth() <= canvasWidth) {
                    drawingBoat.moveRight();
                    return true;
                }
                break;
            case DOWN:
                if (drawingBoat.getPosY() + step + drawingBoat.getBoatHeight() <= canvasHeight) {
                    drawingBoat.moveDown();
                    return true;
                }
                break;
        }
        return false;
    }

    public Image drawCanvas() {
        if (!sizeSet || drawingBoat == null) return null;

        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, canvasWidth, canvasHeight);
        drawingBoat.drawTransport(g);
        g.dispose();

        return image;
    }

    public DrawingBoat getDrawingBoat() {
        return drawingBoat;
    }
}