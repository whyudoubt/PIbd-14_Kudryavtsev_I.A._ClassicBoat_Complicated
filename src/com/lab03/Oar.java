package com.lab03;

import java.awt.*;

public class Oar implements IOarDrawer {
    private OarCount oarCount;

    public Oar(OarCount count) {
        this.oarCount = count;
    }

    @Override
    public void setOarCount(int value) {
        this.oarCount = OarCount.fromInt(value);
    }

    @Override
    public int getOarCount() {
        return oarCount.getValue();
    }

    @Override
    public void draw(Graphics g, int boatX, int boatY, int boatWidth, int boatHeight, Color oarColor) {
        g.setColor(oarColor);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));

        int count = oarCount.getValue();
        int spacing = boatWidth / (count + 1);

        for (int i = 1; i <= count; i++) {
            int oarX = boatX + i * spacing;
            int oarYTop = boatY + boatHeight / 3;
            int oarYBottom = boatY + boatHeight * 2 / 3;

            g.drawLine(oarX, oarYTop, oarX - 15, oarYBottom);
            g.fillOval(oarX - 18, oarYBottom - 4, 8, 8);
        }
    }
}