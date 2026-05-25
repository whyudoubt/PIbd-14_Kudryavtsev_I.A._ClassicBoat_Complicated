package com.lab03;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Вёсла с разноцветными лопастями (цвета фиксируются при создании)
public class OarWithColorfulBlade implements IOarDrawer {
    private OarCount oarCount;
    private final List<Color> bladeColors;

    public OarWithColorfulBlade(OarCount count) {
        this.oarCount = count;
        this.bladeColors = new ArrayList<>();

        // Генерируем цвета для всех лопастей один раз при создании
        Random random = new Random();
        for (int i = 0; i < oarCount.getValue(); i++) {
            bladeColors.add(new Color(
                    random.nextInt(256),
                    random.nextInt(256),
                    random.nextInt(256)
            ));
        }
    }

    @Override
    public void setOarCount(int value) {
        OarCount newCount = OarCount.fromInt(value);
        if (this.oarCount != newCount) {
            this.oarCount = newCount;
            // При изменении количества вёсел перегенерируем цвета
            bladeColors.clear();
            Random random = new Random();
            for (int i = 0; i < oarCount.getValue(); i++) {
                bladeColors.add(new Color(
                        random.nextInt(256),
                        random.nextInt(256),
                        random.nextInt(256)
                ));
            }
        }
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

        for (int i = 0; i < count; i++) {
            int oarX = boatX + (i + 1) * spacing;
            int oarYTop = boatY + boatHeight / 3;
            int oarYBottom = boatY + boatHeight * 2 / 3;

            // Рисуем весло (коричневое)
            g.setColor(oarColor);
            g.drawLine(oarX, oarYTop, oarX - 15, oarYBottom);

            // Рисуем лопасть фиксированным цветом (не случайным каждый раз)
            Color bladeColor = bladeColors.get(i);
            g.setColor(bladeColor);
            g.fillOval(oarX - 18, oarYBottom - 4, 8, 8);
            g.setColor(Color.BLACK);
            g.drawOval(oarX - 18, oarYBottom - 4, 8, 8);
        }
    }
}