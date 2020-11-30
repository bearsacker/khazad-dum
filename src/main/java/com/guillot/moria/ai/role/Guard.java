package com.guillot.moria.ai.role;

import java.io.Serializable;

import com.guillot.moria.ai.Path;
import com.guillot.moria.character.Monster;
import com.guillot.moria.views.GameState;

public class Guard implements Rolable, Serializable {

    private static final long serialVersionUID = 7438342314853159043L;

    @Override
    public void takeDecision(GameState game, Monster monster) {
        double distanceFromPlayer = game.getPlayer().getPosition().distanceFrom(monster.getPosition());

        if (distanceFromPlayer <= monster.getLightRadius()) {
            if (monster.canAttack(game.getPlayer().getPosition())) {
                monster.setTarget(game.getPlayer());
            } else {
                Path path = game.getDungeon().findPathNear(monster.getPosition(), game.getPlayer().getPosition(), monster.getLightRadius());
                if (path != null) {
                    monster.setTarget(game.getPlayer());
                    monster.setPath(path);
                }
            }
        }
    }

}
