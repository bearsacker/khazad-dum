package com.guillot.moria.utils;

import org.newdawn.slick.Image;

public class ImageBuffer {

    private boolean[][] buffer;

    private int width;

    private int height;

    public ImageBuffer(Image image) {
        width = image.getWidth();
        height = image.getHeight();
        buffer = new boolean[image.getWidth()][image.getHeight()];

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (image.getColor(i, j).a == 1f) {
                    buffer[i][j] = true;
                }
            }
        }
    }

    public boolean getBufferAt(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return buffer[x][y];
        }

        return false;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
