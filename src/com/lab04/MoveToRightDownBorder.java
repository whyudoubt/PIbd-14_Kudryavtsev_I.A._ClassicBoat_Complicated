package com.lab04;

// Стратегия перемещения объекта в правый нижний угол поля
public class MoveToRightDownBorder extends BaseTemplateMovement {
    @Override
    protected boolean isTargetDestination() {
        ObjectCoordinates obj = getObjectCoordinates();
        if (obj == null) return false;

        int step = getStep();
        // Проверка, находится ли правый нижний угол объекта в пределах шага от границ поля
        return Math.abs(obj.getRightBorder() - fieldWidth) <= step &&
                Math.abs(obj.getDownBorder() - fieldHeight) <= step;
    }

    @Override
    protected void moveToTarget() {
        ObjectCoordinates obj = getObjectCoordinates();
        if (obj == null) return;

        int step = getStep();

        // Смещение по горизонтали
        int diffX = obj.getRightBorder() - fieldWidth;
        if (Math.abs(diffX) > step) {
            if (diffX > 0) moveLeft();
            else moveRight();
        }

        // Смещение по вертикали
        int diffY = obj.getDownBorder() - fieldHeight;
        if (Math.abs(diffY) > step) {
            if (diffY > 0) moveUp();
            else moveDown();
        }
    }
}