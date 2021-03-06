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
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.guillot.engine.utils.RandomCollection;
import com.guillot.moria.ai.role.Guard;
import com.guillot.moria.ai.role.Patroller;
import com.guillot.moria.ai.role.Rolable;
import com.guillot.moria.item.Equipable;
import com.guillot.moria.item.ItemType;
import com.guillot.moria.resources.Images;
import com.guillot.moria.utils.RNG;
import com.guillot.moria.views.GameState;

public class Monster extends AbstractCharacter {

    private static final long serialVersionUID = 5134782804305371108L;

    private MonsterRace race;

    private Rolable role;

    private boolean sleeping;

    public static List<MonsterRace> getValuesPerLevel() {
        return asList(
                new MonsterRace("Goblin", 1, 10, 20, 20, 5, 50, 1, asList(ONE_HANDED_WEAPON), Images.GOBLIN), // 31xp
                new MonsterRace("Orc", 3, 20, 20, 40, 5, 60, 2, asList(ONE_HANDED_WEAPON), Images.ORC), // 123xp
                new MonsterRace("Hill orc", 3, 20, 30, 40, 5, 60, 3, asList(BOW, HELMET), Images.HILL_ORC), // 153xp
                new MonsterRace("Mordor orc", 5, 25, 20, 40, 7, 50, 4, asList(ONE_HANDED_WEAPON, SHIELD), Images.MORDOR_ORC), // 230xp
                new MonsterRace("Uruk-hai", 7, 30, 20, 60, 5, 60, 5, asList(ONE_HANDED_WEAPON, ARMOR), Images.URUK_HAI), // 357xp
                new MonsterRace("Orc captain", 8, 35, 20, 50, 7, 50, 5, asList(ONE_HANDED_WEAPON, ARMOR, HELMET), Images.ORC_CAPTAIN), // 448xp
                new MonsterRace("War orc", 9, 40, 30, 75, 8, 60, 5, asList(TWO_HANDED_WEAPON, ARMOR), Images.WAR_ORC), // 639xp
                new MonsterRace("Great orc", 10, 45, 20, 100, 5, 40, 10, asList(BOW, ARMOR), Images.GREAT_ORC), // 660xp
                new MonsterRace("Troll", 13, 50, 20, 200, 5, 20, 20, asList(TWO_HANDED_WEAPON), Images.GOBLIN), // 923xp
                new MonsterRace("Goblin King", 15, 40, 0, 150, 10, 100, 10, asList(STAFF, RING, AMULET), Images.GOBLIN), // 840xp
                new MonsterRace("Balrog", 20, 100, 0, 500, 100, 50, 5, asList(), Images.GOBLIN)); // 2020xp
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
        super(race.getName(), race.getImage());

        this.race = race;
        role = RNG.get().randomNumber(2) == 1 ? new Guard() : new Patroller();
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
    public String getRaceName() {
        return "Monster";
    }

    public void takeDecision(GameState game) {
        role.takeDecision(game, this);
    }

    public boolean isSleeping() {
        return sleeping;
    }

    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }

    @Override
    public boolean isActing() {
        return isMoving() || target != null;
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
