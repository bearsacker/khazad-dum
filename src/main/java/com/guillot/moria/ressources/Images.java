package com.guillot.moria.ressources;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public enum Images {
    LOGO("sprites/logo.png"), //
    LOGO_TEXT("sprites/logo-text.png"), //

    GRANITE_WALL("sprites/granite_wall.png", 64, 96), //
    MAGMA_WALL("sprites/magma_wall.png", 64, 96), //
    QUARTZ_WALL("sprites/quartz_wall.png", 64, 96), //
    ROOM_FLOOR("sprites/room_floor.png"), //
    CORRIDOR_FLOOR("sprites/corridor_floor.png"), //
    STAIRS("sprites/stairs.png", 64, 96), //

    DOOR("sprites/door.png", 64, 96), //

    PILLAR("sprites/pillar.png"), //
    RUBBLE("sprites/rubble.png", 64, 96), //

    HUMAN("sprites/human.png", 64, 96), //
    ELF("sprites/elf.png", 64, 96), //
    DWARF("sprites/dwarf.png", 64, 96), //
    HOBBIT("sprites/hobbit.png", 64, 96), //
    MONSTER("sprites/elf.png", 64, 96), //

    CURSOR("sprites/cursor.png", 64, 96), //
    PARCHMENT("sprites/parchment.png"), //
    MAP_CURSOR("sprites/map_cursor.png"), //

    MAGICAL_OVERLAY("sprites/magical.png"), //
    WINDOW_HEADER_ALTERNATE("gui/window_header_alternate.png"), //
    STONE("gui/stone.png"), //

    MINOR_HEALTH_POTION("sprites/potion.png", 32, 32, 0, 15), //
    LESSER_HEALTH_POTION("sprites/potion.png", 32, 32, 0, 5), //
    HEALTH_POTION("sprites/potion.png", 32, 32, 0, 4), //
    GREATER_HEALTH_POTION("sprites/potion.png", 32, 32, 0, 0), //
    MAJOR_HEALTH_POTION("sprites/potion.png", 32, 32, 0, 11), //

    BAG("sprites/quest.png", 32, 32, 10, 16), //
    MAP("sprites/quest.png", 32, 32, 1, 17), //
    INVENTORY("sprites/quest.png", 32, 32, 11, 16), //
    CHARACTER("sprites/skill.png", 32, 32, 15, 7), //
    LEVEL_UP("sprites/skill.png", 32, 32, 11, 10), //
    MENU("sprites/quest.png", 32, 32, 10, 13), //
    LOCK_PICKING("sprites/skill.png", 32, 32, 12, 3), //
    KEY("sprites/material.png", 32, 32, 7, 3), //

    PHYSICAL_DAMAGES("sprites/skill.png", 32, 32, 3, 3), //
    FIRE_DAMAGES("sprites/skill.png", 32, 32, 0, 0), //
    FROST_DAMAGES("sprites/skill.png", 32, 32, 8, 0), //
    LIGHTNING_DAMAGES("sprites/skill.png", 32, 32, 12, 4), //

    COPPER_1("sprites/material.png", 32, 32, 5, 3), //
    COPPER_2("sprites/material.png", 32, 32, 4, 3), //
    COPPER_3("sprites/material.png", 32, 32, 3, 3), //
    SILVER_1("sprites/material.png", 32, 32, 5, 5), //
    SILVER_2("sprites/material.png", 32, 32, 4, 5), //
    SILVER_3("sprites/material.png", 32, 32, 3, 5), //
    GOLD_1("sprites/material.png", 32, 32, 5, 6), //
    GOLD_2("sprites/material.png", 32, 32, 4, 6), //
    GOLD_3("sprites/material.png", 32, 32, 3, 6), //
    GARNETS("sprites/material.png", 32, 32, 4, 0), //
    GOLD("sprites/material.png", 32, 32, 0, 7), //
    SAPPHIRES("sprites/material.png", 32, 32, 2, 0), //
    RUBIES("sprites/material.png", 32, 32, 1, 0), //
    DIAMONDS("sprites/material.png", 32, 32, 7, 0), //
    EMERALDS("sprites/material.png", 32, 32, 0, 0), //
    OPALS("sprites/material.png", 32, 32, 9, 0), //
    MITHRIL("sprites/material.png", 32, 32, 10, 0), //

    BUCKLER("sprites/shield.png", 32, 32, 0, 0), //
    SMALL_SHIELD("sprites/shield.png", 32, 32, 2, 0), //
    LARGE_SHIELD("sprites/shield.png", 32, 32, 7, 0), //
    KITE_SHIELD("sprites/shield.png", 32, 32, 1, 3), //
    TOWER_SHIELD("sprites/shield.png", 32, 32, 5, 0), //
    GOTHIC_SHIELD("sprites/shield.png", 32, 32, 2, 3), //

    WAR_HAMMER("sprites/weapon.png", 32, 32, 12, 5), //
    BROAD_AXE("sprites/weapon.png", 32, 32, 8, 18), //
    MAUL("sprites/weapon.png", 32, 32, 11, 5), //
    BATTLE_AXE("sprites/weapon.png", 32, 32, 1, 18), //
    GREAT_AXE("sprites/weapon.png", 32, 32, 15, 3), //
    TWO_HANDED_SWORD("sprites/weapon.png", 32, 32, 1, 12), //
    GREAT_SWORD("sprites/weapon.png", 32, 32, 1, 15), //

    CAP("sprites/helmet.png", 32, 32, 1, 0), //
    GREAT_CAP("sprites/helmet.png", 32, 32, 4, 0), //
    HELM("sprites/helmet.png", 32, 32, 7, 0), //
    FULL_HELM("sprites/helmet.png", 32, 32, 8, 0), //
    CROWN("sprites/helmet.png", 32, 32, 9, 1), //
    GREAT_HELM("sprites/helmet.png", 32, 32, 9, 0), //

    SHORT_STAFF("sprites/weapon.png", 32, 32, 0, 8), //
    LONG_STAFF("sprites/weapon.png", 32, 32, 11, 8), //
    COMPOSITE_STAFF("sprites/weapon.png", 32, 32, 6, 6), //
    QUARTER_STAFF("sprites/weapon.png", 32, 32, 12, 6), //
    WAR_STAFF("sprites/weapon.png", 32, 32, 5, 20), //

    AMULET_1("sprites/accessory.png", 32, 32, 3, 0), //
    AMULET_2("sprites/accessory.png", 32, 32, 7, 0), //
    AMULET_3("sprites/accessory.png", 32, 32, 3, 4), //
    AMULET_4("sprites/accessory.png", 32, 32, 4, 0), //
    AMULET_5("sprites/accessory.png", 32, 32, 5, 0), //

    RING_1("sprites/ring.png", 32, 32, 4, 8), //
    RING_2("sprites/ring.png", 32, 32, 13, 8), //
    RING_3("sprites/ring.png", 32, 32, 11, 8), //
    RING_4("sprites/ring.png", 32, 32, 10, 8), //
    RING_5("sprites/ring.png", 32, 32, 7, 8), //

    SHORT_BOW("sprites/weapon.png", 32, 32, 0, 10), //
    LONG_BOW("sprites/weapon.png", 32, 32, 0, 11), //
    HUNTER_BOW("sprites/weapon.png", 32, 32, 4, 11), //
    COMPOSITE_BOW("sprites/weapon.png", 32, 32, 2, 11), //
    SHORT_BATTLE_BOW("sprites/weapon.png", 32, 32, 6, 11), //
    LONG_BATTLE_BOW("sprites/weapon.png", 32, 32, 3, 21), //
    SHORT_WAR_BOW("sprites/weapon.png", 32, 32, 15, 11), //
    LONG_WAR_BOW("sprites/weapon.png", 32, 32, 2, 21), //

    DAGGER("sprites/weapon.png", 32, 32, 12, 1), //
    SMALL_AXE("sprites/weapon.png", 32, 32, 0, 3), //
    AXE("sprites/weapon.png", 32, 32, 13, 3), //
    LARGE_AXE("sprites/weapon.png", 32, 32, 12, 3), //
    SHORT_SWORD("sprites/weapon.png", 32, 32, 11, 0), //
    SABRE("sprites/weapon.png", 32, 32, 1, 13), //
    SCIMITAR("sprites/weapon.png", 32, 32, 4, 12), //
    BLADE("sprites/weapon.png", 32, 32, 3, 12), //
    FALCHION("sprites/weapon.png", 32, 32, 1, 12), //
    LONG_SWORD("sprites/weapon.png", 32, 32, 0, 0), //
    CLAYMORE("sprites/weapon.png", 32, 32, 0, 15), //
    BROAD_SWORD("sprites/weapon.png", 32, 32, 12, 0), //
    BASTARD_SWORD("sprites/weapon.png", 32, 32, 11, 0), //
    CLUB("sprites/weapon.png", 32, 32, 13, 5), //
    SPIKED_CLUB("sprites/weapon.png", 32, 32, 2, 16), //
    MACE("sprites/weapon.png", 32, 32, 7, 13), //
    MORNING_STAR("sprites/weapon.png", 32, 32, 14, 5), //
    FLAIL("sprites/weapon.png", 32, 32, 15, 5), //

    RAGS("sprites/armor.png", 32, 32, 2, 0), //
    CLOAK("sprites/armor.png", 32, 32, 9, 2), //
    ROBE("sprites/armor.png", 32, 32, 0, 0), //
    QUILTED_ARMOR("sprites/armor.png", 32, 32, 1, 0), //
    LEATHER_ARMOR("sprites/armor.png", 32, 32, 5, 0), //
    HARD_LEATHER_ARMOR("sprites/armor.png", 32, 32, 4, 0), //
    STUDDED_LEATHER_ARMOR("sprites/armor.png", 32, 32, 0, 7), //
    RING_MAIL("sprites/armor.png", 32, 32, 6, 0), //
    CHAIN_MAIL("sprites/armor.png", 32, 32, 0, 6), //
    SCALE_MAIL("sprites/armor.png", 32, 32, 6, 8), //
    SPLINT_MAIL("sprites/armor.png", 32, 32, 5, 8), //
    BREAST_PLATE("sprites/armor.png", 32, 32, 0, 8), //
    FIELD_PLATE("sprites/armor.png", 32, 32, 7, 0), //
    GOTHIC_PLATE("sprites/armor.png", 32, 32, 9, 8), //
    FULL_PLATE_MAIL("sprites/armor.png", 32, 32, 8, 0);

    private Image image;

    private int frameWidth;

    private int frameHeight;

    private Images(String path) {
        this(path, 0, 0);
    }

    private Images(String path, int frameWidth, int frameHeight) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        try {
            image = new Image(path);
            image.setFilter(Image.FILTER_NEAREST);
        } catch (SlickException e) {
        }
    }

    private Images(String path, int frameWidth, int frameHeight, int x, int y) {
        try {
            image = new Image(path).getSubImage(x * frameWidth, y * frameHeight, frameWidth, frameHeight);
            image.setFilter(Image.FILTER_NEAREST);
        } catch (SlickException e) {
        }
    }

    public Image getImage() {
        return image;
    }

    public Image getSubImage(int x, int y) {
        return image.getSubImage(x * frameWidth, y * frameHeight, frameWidth, frameHeight);
    }
}
