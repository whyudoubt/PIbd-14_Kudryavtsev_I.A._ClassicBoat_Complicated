package com.lab03;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

// Гавань для лодок (конкретная реализация компании)
public class HarborCompany extends AbstractCompany {

    public HarborCompany(int pictureWidth, int pictureHeight,
                         ICollectionGenericObjects<DrawingBoat> collection) {
        super(pictureWidth, pictureHeight, 125, 55, collection);
    }

    @Override
    protected void drawBackground(Graphics2D g) {
        // Заливка фона
        g.setColor(java.awt.Color.CYAN);
        g.fillRect(0, 0, pictureWidth, pictureHeight);

        // Рисуем сетку
        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(1));

        int cols = (int) Math.floor((double) pictureWidth / placeSizeWidth);
        int rows = (int) Math.floor((double) pictureHeight / placeSizeHeight);

        // Вертикальные линии
        for (int col = 0; col <= cols; col++) {
            int x = col * placeSizeWidth;
            g.drawLine(x, 0, x, pictureHeight);
        }

        // Горизонтальные линии
        for (int row = 0; row <= rows; row++) {
            int y = row * placeSizeHeight;
            g.drawLine(0, y, pictureWidth, y);
        }
    }

    @Override
    protected void drawObjects(Graphics2D g) {
        for (int i = 0; i < collection.getMaxCount(); i++) {
            DrawingBoat boat = collection.getObject(i);
            if (boat != null) {
                Point pos = getPositionByIndex(i);

                // Выравнивание по левому нижнему углу ячейки
                int drawX = pos.x + 2;
                int drawY = pos.y + (placeSizeHeight - boat.getBoatHeight());

                boat.setPosition(drawX, drawY);
                boat.drawTransport(g);
            }
        }
    }
}