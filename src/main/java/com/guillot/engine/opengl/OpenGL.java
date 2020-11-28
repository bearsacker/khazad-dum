package com.guillot.engine.opengl;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class OpenGL {

    public static void drawRectangle(int x, int y, int width, int height, int textureID) {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, textureID);
        glBegin(GL_QUADS);
        glTexCoord2f(0.0f, 1.0f);
        glVertex2f(x, y);
        glTexCoord2f(1.0f, 1.0f);
        glVertex2f(x + width, y);
        glTexCoord2f(1.0f, 0.0f);
        glVertex2f(x + width, y + height);
        glTexCoord2f(0.0f, 0.0f);
        glVertex2f(x, y + height);
        glEnd();
    }
}
