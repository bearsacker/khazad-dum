package com.guillot.moria.views;

import static com.guillot.moria.dungeon.Tile.OPEN_DOOR;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.imageout.ImageOut;

import com.guillot.engine.Game;
import com.guillot.engine.gui.View;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.dungeon.Tile;
import com.guillot.moria.utils.DepthBufferedImage;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;

public class GameView extends View {

    public final static int SIZE = 64;

    private Dungeon dungeon;

    private DepthBufferedImage image;

    private boolean first;

    @Override
    public void start() throws Exception {
        // 1603901822915 door bug
        RNG.get().setSeed(1603901822915L);

        dungeon = new Dungeon(100);
        dungeon.generate();

        int size = (int) Math.sqrt(dungeon.getWidth() * dungeon.getWidth() + dungeon.getHeight() * dungeon.getHeight()) * SIZE / 2;
        image = new DepthBufferedImage(size, size / 2);
    }

    @Override
    public void update() throws Exception {
        super.update();

        if (isFocused()) {

        }
    }

    @Override
    public void paintComponents(Graphics g) throws Exception {
        image.clear();

        for (int i = dungeon.getHeight() - 1; i >= 0; i--) {
            for (int j = 0; j < dungeon.getWidth(); j++) {
                Tile tile = dungeon.getFloor()[i][j];

                boolean alternate = false;

                if (dungeon.getFloor()[i][j].isWall) {
                    if (i + 1 < dungeon.getHeight() && (dungeon.getFloor()[i + 1][j].isFloor || dungeon.getFloor()[i + 1][j].isDoor)) {
                        alternate = true;
                    } else if (j - 1 >= 0 && (dungeon.getFloor()[i][j - 1].isFloor || dungeon.getFloor()[i][j - 1].isDoor)) {
                        alternate = true;
                    } else if (i + 1 < dungeon.getHeight() && j - 1 >= 0
                            && (dungeon.getFloor()[i + 1][j - 1].isFloor || dungeon.getFloor()[i + 1][j - 1].isDoor)) {
                        alternate = true;
                    }
                } else if (dungeon.getFloor()[i][j] == OPEN_DOOR) {
                    alternate = true;
                }

                drawTile(tile, i, j, alternate);
            }
        }

        g.drawImage(image.getImage(), 0, 0);

        if (!first) {
            ImageOut.write(image.getImage(), "out.png");
            first = true;
        }

        super.paintComponents(g);
    }

    private void drawTile(Tile tile, int px, int py, boolean alternate) {
        int x = px * 32 + py * 32;
        int y = py * 16 - px * 16;

        if (tile.image != null) {
            image.drawImage(new Point(x, y), tile.image, x, y, alternate);
        }
    }

    public static void main(String[] args) throws SlickException {
        new Game("Moria", new GameView());
    }

}
