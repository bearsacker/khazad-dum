package com.guillot.moria.utils;

import org.newdawn.slick.SlickException;

public class DepthBuffer<T> {

    private Object[][] depthBuffer;

    private int width;

    private int height;

    public DepthBuffer(int width, int height) throws SlickException {
        this.width = width;
        this.height = height;
        depthBuffer = new Object[width][height];
    }

    public void clear() {
        depthBuffer = new Object[width][height];
    }

    public void drawImageBuffer(T object, ImageBuffer image, float x, float y) {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                if (image.getBufferAt(i, j)) {
                    int bufferX = (int) (x + i);
                    int bufferY = (int) (y + j);

                    if (bufferX >= 0 && bufferY >= 0 && bufferX < width && bufferY < height) {
                        depthBuffer[bufferX][bufferY] = object;
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public T getDepth(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return (T) depthBuffer[x][y];
        }

        return null;
    }

}
