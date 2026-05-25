package com.lab05;

import java.awt.*;

public class HarborCompany extends AbstractCompany {
    private StorageCompanies storage;
    private String companyKey;

    public HarborCompany(int pictureWidth, int pictureHeight,
                         ICollectionGenericObjects<DrawingBoat> collection) {
        super(pictureWidth, pictureHeight, 125, 55, collection);
    }

    public void setStorageReference(StorageCompanies storage, String key) {
        this.storage = storage;
        this.companyKey = key;
    }

    @Override
    protected void onBoatRemoved(DrawingBoat removedBoat) {
        if (storage != null) {
            storage.addToDeletedBoats(removedBoat);
        }
    }

    @Override
    protected void drawBackground(Graphics2D g) {
        // Бирюзовый фон (Cyan) как в усложнённых лабораторных
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, pictureWidth, pictureHeight);

        // Сетка
        g.setColor(Color.GRAY);
        g.setStroke(new BasicStroke(1));

        int cols = (int) Math.floor((double) pictureWidth / placeSizeWidth);
        int rows = (int) Math.floor((double) pictureHeight / placeSizeHeight);

        for (int col = 0; col <= cols; col++) {
            int x = col * placeSizeWidth;
            g.drawLine(x, 0, x, pictureHeight);
        }

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

                int drawX = pos.x + 2;
                int drawY = pos.y + (placeSizeHeight - boat.getBoatHeight());

                boat.setPosition(drawX, drawY);
                boat.drawTransport(g);
            }
        }
    }
}