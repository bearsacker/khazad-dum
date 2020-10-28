package com.guillot.moria;

import com.guillot.moria.dungeon.Dungeon;

public class Main {

    public static void main(String[] args) {
        Dungeon dungeon = new Dungeon(100);
        dungeon.generate();

        for (int i = 0; i < dungeon.getHeight(); i++) {
            for (int j = 0; j < dungeon.getWidth(); j++) {
                switch (dungeon.getFloor()[i][j]) {
                case CLOSED_DOOR:
                    System.out.print("C");
                    break;
                case OPEN_DOOR:
                    System.out.print("O");
                    break;
                case SECRET_DOOR:
                    System.out.print("?");
                    break;
                case CORRIDOR_FLOOR:
                    System.out.print(" ");
                    break;
                case DARK_FLOOR:
                    System.out.print(".");
                    break;
                case LIGHT_FLOOR:
                    System.out.print(" ");
                    break;
                case GRANITE_WALL:
                    System.out.print("#");
                    break;
                case PILLAR:
                    System.out.print("I");
                    break;
                case MAGMA_WALL:
                    System.out.print("M");
                    break;
                case QUARTZ_WALL:
                    System.out.print("Q");
                    break;
                case DOWN_STAIR:
                    System.out.print("v");
                    break;
                case UP_STAIR:
                    System.out.print("^");
                    break;
                case NULL:
                case TMP1_WALL:
                case TMP2_WALL:
                default:
                    System.out.print(" ");
                    break;
                }
            }

            System.out.println();
        }
    }

}
