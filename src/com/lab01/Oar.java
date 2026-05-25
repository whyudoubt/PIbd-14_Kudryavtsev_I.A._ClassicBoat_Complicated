package com.lab01;

import java.awt.*;

public class Oar {
    // Закрытое поле от ДопПеречисл
    private OarCount oarCount;

    // Конструктор
    public Oar(OarCount count) {
        this.oarCount = count;
    }

    // Открытое числовое свойство для установки значения в перечисление
    public void setOarCount(int value) {
        this.oarCount = OarCount.fromInt(value);
    }

    public OarCount getOarCount() {
        return oarCount;
    }

    // Метод прорисовки вёсел
    public void draw(Graphics g, int boatX, int boatY, int boatWidth, int boatHeight, Color oarColor) {
        g.setColor(oarColor);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));

        int oarCountValue = oarCount.getValue();

        // Расстояние между вёслами
        int spacing = boatWidth / (oarCountValue + 1);

        for (int i = 1; i <= oarCountValue; i++) {
            int oarX = boatX + i * spacing;
            int oarYTop = boatY + boatHeight / 3;
            int oarYBottom = boatY + boatHeight * 2 / 3;

            // Рисуем весло (линия)
            g.drawLine(oarX, oarYTop, oarX - 15, oarYBottom);

            // Рисуем лопасть весла (овал)
            g.fillOval(oarX - 18, oarYBottom - 4, 8, 8);
        }
    }
}