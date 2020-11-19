package com.guillot.moria.character;

import static com.guillot.moria.ressources.Images.MONSTER;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.guillot.engine.utils.RandomCollection;

public class Monster extends AbstractCharacter {

    private MonsterRace race;

    private boolean sleeping;

    public static List<MonsterRace> getValuesPerLevel() {
        return asList(
                new MonsterRace("Goblin", 1, 10, 20, 20, 5, 50, 1), //
                new MonsterRace("Orc", 4, 20, 20, 40, 5, 60, 2), //
                new MonsterRace("Hill orc", 5, 20, 30, 40, 5, 60, 3), //
                new MonsterRace("Mordor orc", 6, 25, 20, 40, 7, 50, 4), //
                new MonsterRace("Uruk-hai", 7, 30, 20, 60, 5, 60, 5), //
                new MonsterRace("Orc captain", 8, 35, 20, 50, 7, 50, 5), //
                new MonsterRace("War orc", 9, 40, 30, 75, 8, 60, 5), //
                new MonsterRace("Great orc", 10, 45, 20, 100, 5, 40, 10), //
                new MonsterRace("Troll", 13, 50, 20, 200, 5, 20, 20), //
                new MonsterRace("Goblin King", 15, 40, 0, 150, 10, 100, 10), //
                new MonsterRace("Balrog", 20, 100, 0, 500, 100, 50, 5));
    }

    public static MonsterRace pickMonsterRace(int level) {
        List<MonsterRace> races = getValuesPerLevel().stream().filter(x -> x.getLevel() <= level).collect(toList());
        if (!races.isEmpty()) {
            RandomCollection<MonsterRace> random = new RandomCollection<>();
            races.forEach(x -> random.add(x.getLevel(), x));

            return random.next();
        }

        return null;
    }

    public Monster(MonsterRace race) {
        super(race.getName(), MONSTER);

        this.race = race;
        this.level = race.getLevel();

        init();
    }

    @Override
    public String getClassName() {
        return "Monster";
    }

    public boolean isSleeping() {
        return sleeping;
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    @Override
    public int getStrengthMin() {
        return race.getStrength();
    }

    @Override
    public int getAgilityMin() {
        return race.getAgility();
    }

    @Override
    public int getChanceHitMin() {
        return race.getChanceHit();
    }

    @Override
    public int getChanceCriticalHitMin() {
        return race.getChanceCriticalHit();
    }

    @Override
    public int getLifeMin() {
        return race.getLife();
    }

    @Override
    public int getLightRadiusMin() {
        return race.getLightRadius();
    }

    // Not used

    @Override
    public int getSpiritMin() {
        return 0;
    }

    @Override
    public int getDestinyMin() {
        return 0;
    }

    @Override
    public int getChanceMagicFindMin() {
        return 0;
    }

    @Override
    public int getChanceLockPickingMin() {
        return 0;
    }

    @Override
    public int getInventoryLimitMin() {
        return 0;
    }

}
