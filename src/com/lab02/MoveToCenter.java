package com.lab02;

// Стратегия перемещения объекта в центр поля
public class MoveToCenter extends BaseTemplateMovement {
    @Override
    protected boolean isTargetDestination() {
        ObjectCoordinates obj = getObjectCoordinates();
        if (obj == null) return false;

        int step = getStep();
        // Проверка, находится ли центр объекта в пределах шага от центра поля
        return Math.abs(obj.getObjectMiddleHorizontal() - fieldWidth / 2) <= step &&
                Math.abs(obj.getObjectMiddleVertical() - fieldHeight / 2) <= step;
    }

    @Override
    protected void moveToTarget() {
        ObjectCoordinates obj = getObjectCoordinates();
        if (obj == null) return;

        int step = getStep();

        // Смещение по горизонтали
        int diffX = obj.getObjectMiddleHorizontal() - fieldWidth / 2;
        if (Math.abs(diffX) > step) {
            if (diffX > 0) moveLeft();
            else moveRight();
        }

        // Смещение по вертикали
        int diffY = obj.getObjectMiddleVertical() - fieldHeight / 2;
        if (Math.abs(diffY) > step) {
            if (diffY > 0) moveUp();
            else moveDown();
        }
    }
}