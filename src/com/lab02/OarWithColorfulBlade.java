package com.lab02;

import java.awt.*;
import java.util.Random;

public class OarWithColorfulBlade implements IOarDrawer {
    private OarCount oarCount;
    private Random random;

    public OarWithColorfulBlade(OarCount count) {
        this.oarCount = count;
        this.random = new Random();
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

            // Разноцветная лопасть
            Color bladeColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            g.setColor(bladeColor);
            g.fillOval(oarX - 18, oarYBottom - 4, 8, 8);
            g.setColor(Color.BLACK);
            g.drawOval(oarX - 18, oarYBottom - 4, 8, 8);

            g.setColor(oarColor);
        }
    }
}