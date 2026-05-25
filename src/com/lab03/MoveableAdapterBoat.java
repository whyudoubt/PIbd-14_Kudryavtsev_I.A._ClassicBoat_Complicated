package com.lab03;

import java.awt.*;

// Адаптер для связи DrawingBoat с интерфейсом IMoveableObject
public class MoveableAdapterBoat implements IMoveableObject {
    private final DrawingBoat boat;

    public MoveableAdapterBoat(DrawingBoat boat) {
        this.boat = boat;
    }

    @Override
    public ObjectCoordinates getObjectCoordinates() {
        if (boat == null || boat.getPosX() == null || boat.getPosY() == null) {
            return null;
        }
        return new ObjectCoordinates(boat.getPosX(), boat.getPosY(),
                boat.getBoatWidth(), boat.getBoatHeight());
    }

    @Override
    public int getObjectStep() {
        Double step = boat.getBoatStep();
        return step != null ? (int) Math.round(step) : 5;
    }

    @Override
    public void setObjectPosition(int x, int y) {
        boat.setPosition(x, y);
    }

    @Override
    public void moveObject(MovementDirection direction) {
        if (boat == null) return;
        switch (direction) {
            case LEFT -> boat.moveLeft();
            case UP -> boat.moveUp();
            case RIGHT -> boat.moveRight();
            case DOWN -> boat.moveDown();
        }
    }

    @Override
    public void drawObject(Graphics graphics) {
        if (boat != null) {
            boat.drawTransport(graphics);
        }
    }
}