package com.lab04;

import java.awt.*;

public interface IOarDrawer {
    void setOarCount(int count);
    int getOarCount();
    void draw(Graphics g, int boatX, int boatY, int boatWidth, int boatHeight, Color oarColor);
}