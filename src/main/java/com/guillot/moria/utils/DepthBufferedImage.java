package com.guillot.moria.utils;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class DepthBufferedImage {

    private Image image;

    private Graphics graphics;

    private Object[][] depthBuffer;

    public DepthBufferedImage(int width, int height) throws SlickException {
        image = new Image(width, height);
        graphics = image.getGraphics();
        depthBuffer = new Object[width][height];
    }

    public void clear() {
        graphics.clear();
        depthBuffer = new Object[getWidth()][getHeight()];
    }

    public void drawImage(Image image, float x, float y) {
        drawImage(null, image, x, y, Color.white);
    }

    public void drawImage(Image image, float x, float y, Color filter) {
        drawImage(null, image, x, y, filter);
    }

    public void drawImage(Object object, Image image, float x, float y) {
        drawImage(object, image, x, y, Color.white);
    }

    public void drawImage(Object object, Image image, float x, float y, Color filter) {
        graphics.drawImage(image, x, y, filter);

        if (object != null) {
            for (int i = 0; i < image.getWidth(); i++) {
                for (int j = 0; j < image.getHeight(); j++) {
                    if (image.getColor(i, j).a == 1f) {
                        int bufferX = (int) (x + i);
                        int bufferY = (int) (y + j);

                        if (bufferX >= 0 && bufferY >= 0 && bufferX < getWidth() && bufferY < getHeight()) {
                            depthBuffer[bufferX][bufferY] = object;
                        }
                    }
                }
            }
        }
    }

    public void drawImage(Object object, Image image, float x, float y, float x2, float y2, float srcx, float srcy, float srcx2,
            float srcy2) {
        graphics.drawImage(image, x, y, x2, y2, srcx, srcy, srcx2, srcy2);

        for (int i = 0; srcx + i < srcx2; i++) {
            for (int j = 0; srcy + j < srcy2; j++) {
                if (image.getColor((int) (srcx + i), (int) (srcy + j)).a == 1f) {
                    int bufferX = (int) (x + i);
                    int bufferY = (int) (y + j);

                    if (bufferX >= 0 && bufferY >= 0 && bufferX < getWidth() && bufferY < getHeight()) {
                        depthBuffer[bufferX][bufferY] = object;
                    }
                }
            }
        }
    }

    public void drawImage(Image image, float x, float y, float x2, float y2, float srcx, float srcy, float srcx2,
            float srcy2) {
        graphics.drawImage(image, x, y, x2, y2, srcx, srcy, srcx2, srcy2);
    }

    public Image getImage() {
        return image;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public Object getDepth(int x, int y) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            return depthBuffer[x][y];
        }

        return null;
    }

    public float getCenterOfRotationX() {
        return image.getCenterOfRotationX();
    }

    public float getCenterOfRotationY() {
        return image.getCenterOfRotationY();
    }

    public Graphics getGraphics() {
        return graphics;
    }
}
