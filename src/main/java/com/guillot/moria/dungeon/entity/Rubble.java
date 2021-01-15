package com.guillot.moria.dungeon.entity;

import com.guillot.moria.resources.Images;
import com.guillot.moria.utils.Point;

public class Rubble extends AbstractEntity {

    private static final long serialVersionUID = -8720085262878028620L;

    public Rubble(Point position) {
        super(position);

        type = Entity.RUBBLE;
        image = Images.RUBBLE;
    }

}
