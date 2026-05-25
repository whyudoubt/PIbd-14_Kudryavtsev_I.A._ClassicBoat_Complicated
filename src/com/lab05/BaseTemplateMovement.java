package com.lab05;

// Абстрактный класс-шаблон стратегии перемещения
public abstract class BaseTemplateMovement {
    private IMoveableObject moveableObject;
    private TemplateMovementStatus state = TemplateMovementStatus.NOT_INIT;

    protected int fieldWidth;
    protected int fieldHeight;

    // Проверка, достигнута ли цель
    public boolean isFinishReached() {
        return state == TemplateMovementStatus.FINISH;
    }

    // Установка данных для перемещения
    public void setData(IMoveableObject moveableObject, int width, int height) {
        if (moveableObject == null) {
            state = TemplateMovementStatus.NOT_INIT;
            return;
        }
        state = TemplateMovementStatus.IN_PROGRESS;
        this.moveableObject = moveableObject;
        this.fieldWidth = width;
        this.fieldHeight = height;
    }

    // Выполнение одного шага перемещения
    public void makeStep() {
        if (state != TemplateMovementStatus.IN_PROGRESS) return;

        if (isTargetDestination()) {
            state = TemplateMovementStatus.FINISH;
            return;
        }
        moveToTarget();
    }

    // Вспомогательные методы для перемещения
    protected void moveLeft() { moveTo(MovementDirection.LEFT); }
    protected void moveRight() { moveTo(MovementDirection.RIGHT); }
    protected void moveUp() { moveTo(MovementDirection.UP); }
    protected void moveDown() { moveTo(MovementDirection.DOWN); }

    // Получение координат объекта
    protected ObjectCoordinates getObjectCoordinates() {
        return moveableObject != null ? moveableObject.getObjectCoordinates() : null;
    }

    // Получение шага перемещения
    protected int getStep() {
        return moveableObject != null ? moveableObject.getObjectStep() : 5;
    }

    // Абстрактные методы для переопределения в наследниках
    protected abstract void moveToTarget();
    protected abstract boolean isTargetDestination();

    // Перемещение в указанном направлении
    private void moveTo(MovementDirection direction) {
        if (state != TemplateMovementStatus.IN_PROGRESS || moveableObject == null) return;
        moveableObject.moveObject(direction);
    }
}