package com.guillot.moria.ai.role;

import java.io.Serializable;

import com.guillot.moria.ai.Path;
import com.guillot.moria.character.Monster;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;
import com.guillot.moria.views.GameState;

public class Patroller implements Rolable, Serializable {

    private static final long serialVersionUID = 2214343096019693244L;

    @Override
    public void takeDecision(GameState game, Monster monster) {
        double distanceFromPlayer = game.getPlayer().getPosition().distanceFrom(monster.getPosition());

        if (distanceFromPlayer <= monster.getLightRadius()) {
            if (distanceFromPlayer > 1) {
                Path path = game.getDungeon().findPathNear(monster.getPosition(), game.getPlayer().getPosition(), monster.getLightRadius());
                if (path != null) {
                    monster.setTarget(game.getPlayer());
                    monster.setPath(path);
                }
            } else {
                monster.setTarget(game.getPlayer());
            }
        } else if (RNG.get().randomNumber(3) == 1) {
            int tries = 0;

            Path path = null;
            while (path == null) {
                if (tries > 100) {
                    monster.setPath(null);
                    return;
                }

                path = findDestination(game.getDungeon(), monster);
                tries++;
            }

            monster.setPath(path);
        }
    }

    private Path findDestination(Dungeon dungeon, Monster monster) {
        Point destination = placePoint(monster);
        while (!dungeon.coordInBounds(destination)) {
            destination = placePoint(monster);
        }

        return dungeon.findPath(monster.getPosition(), destination, monster.getLightRadius());
    }

    private Point placePoint(Monster monster) {
        int x = RNG.get().randomNumberBetween(monster.getPosition().x - monster.getMovement(),
                monster.getPosition().x + monster.getMovement());
        int y = RNG.get().randomNumberBetween(monster.getPosition().y - monster.getMovement(),
                monster.getPosition().y + monster.getMovement());

        return new Point(x, y);
    }
}
