package com.guillot.moria.dungeon.entity;

import com.guillot.moria.ressources.Images;
import com.guillot.moria.utils.Point;

public class Pillar extends AbstractEntity {

    private static final long serialVersionUID = -3414296673206389378L;

    public Pillar(Point position) {
        super(position);

        type = Entity.PILLAR;
        image = Images.PILLAR;
    }

}
