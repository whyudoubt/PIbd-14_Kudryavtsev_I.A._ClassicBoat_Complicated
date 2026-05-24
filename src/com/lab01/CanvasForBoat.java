package com.lab01;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CanvasForBoat {
    private DrawingBoat drawingBoat;
    private Integer canvasWidth;
    private Integer canvasHeight;

    public void setPictureSize(int width, int height) {
        this.canvasWidth = width;
        this.canvasHeight = height;
    }

    public boolean insertBoat(DrawingBoat boat) {
        if (canvasWidth == null || canvasHeight == null) return false;
        if (boat.getBoatWidth() > canvasWidth || boat.getBoatHeight() > canvasHeight) return false;
        this.drawingBoat = boat;
        return true;
    }

    public void setBoatPosition(int x, int y) {
        if (canvasWidth == null || canvasHeight == null || drawingBoat == null) return;

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
        if (canvasWidth == null || canvasHeight == null ||
                drawingBoat == null || drawingBoat.getPosX() == null ||
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
        if (canvasWidth == null || canvasHeight == null || drawingBoat == null) return null;

        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, canvasWidth, canvasHeight);
        drawingBoat.drawTransport(g2d);
        g2d.dispose();

        return image;
    }
}