package com.guillot.moria.ai.role;

import com.guillot.moria.character.Monster;
import com.guillot.moria.views.GameState;

public interface Rolable {

    void takeDecision(GameState game, Monster monster);
}
