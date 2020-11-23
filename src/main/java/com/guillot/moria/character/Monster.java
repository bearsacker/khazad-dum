package com.guillot.moria.character;

import static com.guillot.moria.item.ItemGenerator.generateItem;
import static com.guillot.moria.item.ItemType.AMULET;
import static com.guillot.moria.item.ItemType.ARMOR;
import static com.guillot.moria.item.ItemType.BOW;
import static com.guillot.moria.item.ItemType.HELMET;
import static com.guillot.moria.item.ItemType.ONE_HANDED_WEAPON;
import static com.guillot.moria.item.ItemType.RING;
import static com.guillot.moria.item.ItemType.SHIELD;
import static com.guillot.moria.item.ItemType.STAFF;
import static com.guillot.moria.item.ItemType.TWO_HANDED_WEAPON;
import static com.guillot.moria.ressources.Images.MONSTER;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.guillot.engine.utils.RandomCollection;
import com.guillot.moria.item.Equipable;
import com.guillot.moria.item.ItemType;

public class Monster extends AbstractCharacter {

    private MonsterRace race;

    private boolean sleeping;

    public static List<MonsterRace> getValuesPerLevel() {
        return asList(
                new MonsterRace("Goblin", 1, 10, 20, 20, 5, 50, 1, asList(ONE_HANDED_WEAPON)), // 31xp
                new MonsterRace("Orc", 3, 20, 20, 40, 5, 60, 2, asList(ONE_HANDED_WEAPON)), // 123xp
                new MonsterRace("Hill orc", 3, 20, 30, 40, 5, 60, 3, asList(BOW, HELMET)), // 153xp
                new MonsterRace("Mordor orc", 5, 25, 20, 40, 7, 50, 4, asList(ONE_HANDED_WEAPON, SHIELD)), // 230xp
                new MonsterRace("Uruk-hai", 7, 30, 20, 60, 5, 60, 5, asList(ONE_HANDED_WEAPON, ARMOR)), // 357xp
                new MonsterRace("Orc captain", 8, 35, 20, 50, 7, 50, 5, asList(ONE_HANDED_WEAPON, ARMOR, HELMET)), // 448xp
                new MonsterRace("War orc", 9, 40, 30, 75, 8, 60, 5, asList(TWO_HANDED_WEAPON, ARMOR)), // 639xp
                new MonsterRace("Great orc", 10, 45, 20, 100, 5, 40, 10, asList(BOW, ARMOR)), // 660xp
                new MonsterRace("Troll", 13, 50, 20, 200, 5, 20, 20, asList(TWO_HANDED_WEAPON)), // 923xp
                new MonsterRace("Goblin King", 15, 40, 0, 150, 10, 100, 10, asList(STAFF, RING, AMULET)), // 840xp
                new MonsterRace("Balrog", 20, 100, 0, 500, 100, 50, 5, asList())); // 2020xp
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
        level = race.getLevel();

        init();

        for (ItemType type : race.getEquipedItemsType()) {
            Equipable item = null;
            do {
                item = (Equipable) generateItem(type, 0, level);
            } while (!item.isEquipable(this));

            equipItem(item);
        }
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
