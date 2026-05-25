package com.lab02;

import java.awt.*;

// Интерфейс для работы с перемещаемым объектом
public interface IMoveableObject {
    // Координаты объекта
    ObjectCoordinates getObjectCoordinates();

    // Шаг перемещения
    int getObjectStep();

    // Установка позиции
    void setObjectPosition(int x, int y);

    // Перемещение в указанном направлении
    void moveObject(MovementDirection direction);

    // Прорисовка объекта
    void drawObject(Graphics graphics);
}