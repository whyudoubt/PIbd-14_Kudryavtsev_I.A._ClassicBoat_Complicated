package com.lab03;

import java.awt.*;
import java.awt.image.BufferedImage;  // ← добавить этот импорт
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Абстрактная компания (гавань для лодок)
public abstract class AbstractCompany {
    protected final int placeSizeWidth;
    protected final int placeSizeHeight;
    protected final int pictureWidth;
    protected final int pictureHeight;
    protected ICollectionGenericObjects<DrawingBoat> collection;

    protected AbstractCompany(int pictureWidth, int pictureHeight,
                              int placeSizeWidth, int placeSizeHeight,
                              ICollectionGenericObjects<DrawingBoat> collection) {
        this.pictureWidth = pictureWidth;
        this.pictureHeight = pictureHeight;
        this.placeSizeWidth = placeSizeWidth;
        this.placeSizeHeight = placeSizeHeight;
        this.collection = collection;

        int maxCount = calcMaxCount();
        collection.setMaxCount(maxCount);
    }

    public int getCountObjects() {
        return collection.getCountObjects();
    }

    public boolean addBoat(DrawingBoat boat) {
        return collection.insertObject(boat);
    }

    public boolean removeBoat(int position) {
        return collection.removeObject(position);
    }

    public void removeLastBoat() {
        for (int i = collection.getMaxCount() - 1; i >= 0; i--) {
            if (collection.getObject(i) != null) {
                collection.removeObject(i);
                break;
            }
        }
    }

    public DrawingBoat getRandomObject() {
        Random random = new Random();
        int maxCount = collection.getCountObjects();
        if (maxCount == 0) return null;

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < collection.getMaxCount(); i++) {
            if (collection.getObject(i) != null) {
                indices.add(i);
            }
        }

        if (indices.isEmpty()) return null;

        int randomIndex = indices.get(random.nextInt(indices.size()));
        return collection.getObject(randomIndex);
    }

    public Image show() {
        // Создаём BufferedImage для рисования
        BufferedImage image = new BufferedImage(pictureWidth, pictureHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();  // ← теперь не красное

        drawBackground(g);
        drawObjects(g);

        g.dispose();
        return image;
    }

    protected abstract void drawBackground(Graphics2D g);
    protected abstract void drawObjects(Graphics2D g);

    private int calcMaxCount() {
        int cols = (int) Math.floor((double) pictureWidth / placeSizeWidth);
        int rows = (int) Math.floor((double) pictureHeight / placeSizeHeight);
        return cols * rows;
    }

    protected Point getPositionByIndex(int index) {
        int cols = (int) Math.floor((double) pictureWidth / placeSizeWidth);
        int row = index / cols;
        int col = index % cols;

        int x = col * placeSizeWidth;
        int y = row * placeSizeHeight;

        return new Point(x, y);
    }
}