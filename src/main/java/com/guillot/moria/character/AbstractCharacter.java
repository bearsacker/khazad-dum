package com.guillot.moria.character;

import static com.guillot.moria.configs.LevelingConfig.LEVELING_LEVELS;
import static com.guillot.moria.configs.LevelingConfig.LEVELING_POINTS_PER_LEVEL;
import static com.guillot.moria.dungeon.Direction.EAST;
import static com.guillot.moria.dungeon.Direction.NORTH;
import static com.guillot.moria.dungeon.Direction.SOUTH;
import static com.guillot.moria.dungeon.Direction.WEST;
import static com.guillot.moria.item.ItemRarity.LEGENDARY;
import static com.guillot.moria.item.ItemRarity.MAGIC;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.Arrays.asList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.moria.ai.Path;
import com.guillot.moria.dungeon.Direction;
import com.guillot.moria.dungeon.Dungeon;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.Equipable;
import com.guillot.moria.item.ItemType;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;
import com.guillot.moria.views.GameState;

public abstract class AbstractCharacter implements Serializable {

    private static final long serialVersionUID = 3072634065144460631L;

    protected String name;

    protected int level;

    protected int xp;

    protected int attributesPoints;

    protected int currentLife;

    protected Images image;

    // Position

    protected Point position;

    protected Direction direction;

    protected Path path;

    protected int currentStep;

    protected boolean acting;

    protected AbstractCharacter target;

    // Animation

    private int currentFrame;

    private long lastAnimation;

    // Attributes

    protected int strengthBase;

    protected int agilityBase;

    protected int spiritBase;

    protected int destinyBase;

    // Computed attributes

    protected int strength;

    protected int agility;

    protected int spirit;

    protected int destiny;

    // Computed from strength

    protected int physicalDamage;

    protected int inventoryLimit;

    // Computed from agility

    protected int chanceHit;

    protected int chanceDodge;

    protected int movement;

    // Computed from spirit

    protected int life;

    protected int lightRadius;

    // Computed from destiny

    protected int chanceMagicFind;

    protected int chanceLockPicking;

    protected int chanceCriticalHit;

    // Computed from equipment

    protected int fireDamage;

    protected int frostDamage;

    protected int lightningDamage;

    protected int damages;

    protected int armor;

    // Equipment

    protected int gold;

    protected ArrayList<AbstractItem> inventory;

    protected Equipable head;

    protected Equipable body;

    protected Equipable leftHand;

    protected Equipable rightHand;

    protected Equipable finger;

    protected Equipable neck;

    protected AbstractCharacter(String name, Images image) {
        this.name = name;
        this.image = image;

        level = 1;
        xp = 0;
        attributesPoints = 0;

        direction = Direction.SOUTH;

        inventory = new ArrayList<>();
    }

    public void init() {
        strengthBase = getStrengthMin();
        agilityBase = getAgilityMin();
        spiritBase = getSpiritMin();
        destinyBase = getDestinyMin();

        computeStatistics();
        currentLife = life;
    }

    public String getName() {
        return name;
    }

    public void computeStatistics() {
        strength = 0;
        agility = 0;
        spirit = 0;
        destiny = 0;

        damages = 1;
        fireDamage = 0;
        frostDamage = 0;
        lightningDamage = 0;
        armor = 0;

        physicalDamage = 0;
        inventoryLimit = 0;

        chanceHit = 0;
        chanceDodge = 0;
        movement = 0;

        life = 0;
        lightRadius = 0;

        chanceMagicFind = 0;
        chanceLockPicking = 0;
        chanceCriticalHit = 0;

        if (head != null) {
            head.equip(this);
        }
        if (neck != null) {
            neck.equip(this);
        }
        if (body != null) {
            body.equip(this);
        }
        if (finger != null) {
            finger.equip(this);
        }
        if (leftHand != null) {
            leftHand.equip(this);
        }
        if (rightHand != null && rightHand != leftHand) {
            rightHand.equip(this);
        }

        strength += strengthBase;
        agility += agilityBase;
        spirit += spiritBase;
        destiny += destinyBase;

        physicalDamage += strength;
        inventoryLimit += getInventoryLimitMin() + strength / 2;

        chanceHit += getChanceHitMin() + agility / 2;
        chanceDodge += agility / 2;
        movement += agility / 3;

        life += getLifeMin() + spirit * 2;
        lightRadius += getLightRadiusMin() + spirit / 4;

        chanceMagicFind += getChanceMagicFindMin() + destiny;
        chanceLockPicking += getChanceLockPickingMin() + destiny;
        chanceCriticalHit += getChanceCriticalHitMin() + destiny / 3;
    }

    public boolean pickUpItem(AbstractItem item) {
        if (!inventory.contains(item) && inventory.size() < inventoryLimit) {
            inventory.add(item);
            return true;
        }

        return false;
    }

    public boolean dropItem(AbstractItem item) {
        return inventory.remove(item);
    }

    public boolean dropItem(Dungeon dungeon, AbstractItem item) {
        if (isEquipedByItem(item)) {
            unequipItem(item);
        }

        List<Point> coords = asList(new Point(position.x, position.y - 1),
                new Point(position.x, position.y + 1),
                new Point(position.x - 1, position.y),
                new Point(position.x + 1, position.y));

        for (Point coord : coords) {
            if (dungeon.getFloor()[coord.y][coord.x].isFloor && !dungeon.isItemAt(coord)) {
                item.setPosition(coord);
                dungeon.getItems().add(item);
                return inventory.remove(item);
            }
        }

        return false;
    }

    public void dropEquipment(Dungeon dungeon) {
        HashSet<AbstractItem> items = new HashSet<>();
        items.add((AbstractItem) leftHand);
        if (leftHand != rightHand) {
            items.add((AbstractItem) rightHand);
        }
        items.add((AbstractItem) body);
        items.add((AbstractItem) head);
        items.add((AbstractItem) neck);
        items.add((AbstractItem) finger);
        items.remove(null);

        for (AbstractItem item : items) {
            unequipItem(item);
            item.setPosition(position);

            dungeon.getItems().add(item);
        }
    }

    public void equipItem(Equipable item) {
        if (item.isEquipable(this)) {
            switch (((AbstractItem) item).getBlock()) {
            case HEAD:
                head = item;
                break;
            case BODY:
                body = item;
                break;
            case LEFT_HAND:
                if (leftHand != null && rightHand == leftHand) {
                    rightHand = null;
                }
                leftHand = item;
                break;
            case RIGHT_HAND:
                if (rightHand != null && rightHand == leftHand) {
                    leftHand = null;
                }
                rightHand = item;
                break;
            case TWO_HANDS:
                leftHand = item;
                rightHand = item;
                break;
            case FINGER:
                finger = item;
                break;
            case NECK:
                neck = item;
                break;
            case NONE:
                break;
            }

            computeStatistics();
        }
    }

    public void unequipItem(AbstractItem item) {
        if (head == item) {
            head = null;
        }
        if (neck == item) {
            neck = null;
        }
        if (body == item) {
            body = null;
        }
        if (finger == item) {
            finger = null;
        }
        if (leftHand == item) {
            leftHand = null;
        }
        if (rightHand == item) {
            rightHand = null;
        }

        computeStatistics();
    }

    public boolean isEquipedByItem(AbstractItem item) {
        return head == item || neck == item || finger == item || body == item || leftHand == item || rightHand == item;
    }

    public boolean hasItemType(ItemType type) {
        return inventory.stream().anyMatch(x -> x.getType() == type);
    }

    public AbstractItem getFirstItemOfType(ItemType type) {
        return inventory.stream().filter(x -> x.getType() == type).findAny().orElse(null);
    }

    public void update(GameState game) {
        if (isMoving()) {
            long time = System.currentTimeMillis();
            if (time - lastAnimation > 50) {
                currentFrame++;
                currentFrame %= 4;

                lastAnimation = time;
            }
        }

        if (path != null) {
            Point newPosition = path.getStep(currentStep).inverseXY();
            currentStep++;

            if (position.x - newPosition.x == 1) {
                direction = NORTH;
            } else if (position.x - newPosition.x == -1) {
                direction = SOUTH;
            } else if (position.y - newPosition.y == 1) {
                direction = WEST;
            } else if (position.y - newPosition.y == -1) {
                direction = EAST;
            }
            setPosition(newPosition);

            if (currentStep > movement || currentStep >= path.getLength()) {
                path = null;
                currentFrame = 0;
            }
        } else if (target != null) {
            Attack attack = doAttack();
            if (attack == null) {
                game.addMessage(getName() + " misses his attack!");
            } else {
                String message = (attack.isCritical() ? "RED_PALE@@Critical hit! @@WHITE@@" : "WHITE@@");
                int damagesReceived = target.takeAHit(attack);
                if (damagesReceived < 0) {
                    message += target.getName() + " dodges the attack!";
                } else {
                    message += target.getName() + " takes @@RED_PALE@@" + damagesReceived + "@@WHITE@@ damages!";
                }

                game.addMessage(message);
            }

            target = null;
        }
    }

    public void draw(Graphics g, Point playerPosition) {
        int x = (position.y - playerPosition.y) * 32 + (position.x - playerPosition.x) * 32 + EngineConfig.WIDTH / 2 - 32;
        int y = (position.x - playerPosition.x) * 16 - (position.y - playerPosition.y) * 16 + EngineConfig.HEIGHT / 2 - 48;

        g.drawImage(this.image.getSubImage(direction.getValue() * 4 + currentFrame, 0), x, y);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getCurrentLife() {
        return currentLife;
    }

    public void setCurrentLife(int currentLife) {
        this.currentLife = max(0, min(currentLife, life));
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getSpirit() {
        return spirit;
    }

    public void setSpirit(int spirit) {
        this.spirit = spirit;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLightRadius() {
        return lightRadius;
    }

    public void setLightRadius(int lightRadius) {
        this.lightRadius = lightRadius;
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public int getPhysicalDamage() {
        return physicalDamage;
    }

    public void setPhysicalDamage(int physicalDamage) {
        this.physicalDamage = physicalDamage;
    }

    public int getFireDamage() {
        return fireDamage;
    }

    public void setFireDamage(int fireDamage) {
        this.fireDamage = fireDamage;
    }

    public int getFrostDamage() {
        return frostDamage;
    }

    public void setFrostDamage(int frostDamage) {
        this.frostDamage = frostDamage;
    }

    public int getLightningDamage() {
        return lightningDamage;
    }

    public void setLightningDamage(int lightningDamage) {
        this.lightningDamage = lightningDamage;
    }

    public int getChanceHit() {
        return chanceHit;
    }

    public void setChanceHit(int chanceHit) {
        this.chanceHit = max(0, min(chanceHit, 100));
    }

    public int getChanceDodge() {
        return chanceDodge;
    }

    public void setChanceDodge(int chanceDodge) {
        this.chanceDodge = max(0, min(chanceDodge, 100));
    }

    public int getChanceMagicFind() {
        return chanceMagicFind;
    }

    public void setChanceMagicFind(int chanceMagicFind) {
        this.chanceMagicFind = max(0, min(chanceMagicFind, 100));
    }

    public int getChanceLockPicking() {
        return chanceLockPicking;
    }

    public void setChanceLockPicking(int chanceLockPicking) {
        this.chanceLockPicking = max(0, min(chanceLockPicking, 100));
    }

    public int getChanceCriticalHit() {
        return chanceCriticalHit;
    }

    public void setChanceCriticalHit(int chanceCriticalHit) {
        this.chanceCriticalHit = max(0, min(chanceCriticalHit, 100));
    }

    public int getInventoryLimit() {
        return inventoryLimit;
    }

    public void setInventoryLimit(int inventoryLimit) {
        this.inventoryLimit = inventoryLimit;
    }

    public int getDamages() {
        return damages;
    }

    public void setDamages(int damages) {
        this.damages = damages;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDestiny() {
        return destiny;
    }

    public void setDestiny(int destiny) {
        this.destiny = destiny;
    }

    public ArrayList<AbstractItem> getInventory() {
        return inventory;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = new Point(position);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public Equipable getHead() {
        return head;
    }

    public Equipable getBody() {
        return body;
    }

    public Equipable getLeftHand() {
        return leftHand;
    }

    public Equipable getRightHand() {
        return rightHand;
    }

    public Equipable getFinger() {
        return finger;
    }

    public Equipable getNeck() {
        return neck;
    }

    public void increaseStrength() {
        if (attributesPoints > 0) {
            strengthBase++;
            attributesPoints--;
            computeStatistics();
        }
    }

    public void increaseAgility() {
        if (attributesPoints > 0) {
            agilityBase++;
            attributesPoints--;
            computeStatistics();
        }
    }

    public void increaseSpirit() {
        if (attributesPoints > 0) {
            spiritBase++;
            attributesPoints--;
            computeStatistics();
        }
    }

    public void increaseDestiny() {
        if (attributesPoints > 0) {
            destinyBase++;
            attributesPoints--;
            computeStatistics();
        }
    }

    public int computeDamages() {
        return computePhysicalDamages() + fireDamage + frostDamage + lightningDamage;
    }

    public int computePhysicalDamages() {
        return (int) (damages + (physicalDamage / 100f) * damages);
    }

    public boolean canAttack(Point targetPosition) {
        double distance = position.distanceFrom(targetPosition);

        return distance == 1 || (leftHand != null && ((AbstractItem) leftHand).getType() == ItemType.BOW && distance <= getLightRadius());
    }

    public Attack doAttack() {
        if (RNG.get().randomNumber(100) > chanceHit) {
            return null;
        }

        boolean critical = RNG.get().randomNumber(100) < chanceCriticalHit;

        return new Attack(computePhysicalDamages(), fireDamage, frostDamage, lightningDamage, critical);
    }

    public int takeAHit(Attack attack) {
        if (RNG.get().randomNumber(100) < chanceDodge) {
            return -1;
        }

        int damagesReceived = max(0, (attack.isCritical() ? 2 : 1) * attack.getPhysicalDamages() - armor) + attack.getFireDamages()
                + attack.getFrostDamages() + attack.getLightningDamages();
        setCurrentLife(getCurrentLife() - damagesReceived);

        return damagesReceived;
    }

    public boolean isDead() {
        return currentLife == 0;
    }

    public int getXPValue() {
        return level * (strength + agility + destiny + spirit + damages + armor);
    }

    public int getAttributesPoints() {
        return attributesPoints;
    }

    public int earnXP(int gain) {
        xp += gain;
        int max = LEVELING_LEVELS[level - 1];

        if (xp >= max) {
            int remainder = xp - max;
            xp = 0;
            level++;
            attributesPoints += LEVELING_POINTS_PER_LEVEL;

            return 1 + earnXP(remainder);
        }

        return 0;
    }

    public void setPath(Path path) {
        this.path = path;
        currentStep = 0;
    }

    public Point getDestination() {
        return path != null ? path.getDestination().inverseXY() : position;
    }

    public boolean isMoving() {
        return path != null;
    }

    public void setActing(boolean acting) {
        this.acting = acting;
    }

    public boolean isActing() {
        return acting;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(AbstractCharacter target) {
        this.target = target;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        String text = "RED_PALE@@Strength: @@WHITE@@" + strength + "\n";
        text += "LIGHT_GREY@@     Physical damage: @@WHITE@@+ " + physicalDamage + "%\n";
        text += "LIGHT_GREY@@     Inventory limit: @@WHITE@@" + inventoryLimit + " blocks\n\n";

        text += "GREEN_PALE@@Agility: @@WHITE@@" + agility + "\n";
        text += "LIGHT_GREY@@     Chance to hit: @@WHITE@@" + chanceHit + "%\n";
        text += "LIGHT_GREY@@     Chance to block/dodge: @@WHITE@@" + chanceDodge + "%\n";
        text += "LIGHT_GREY@@     Movement: @@WHITE@@" + movement + "m\n\n";

        text += "YELLOW_PALE@@Spirit: @@WHITE@@" + spirit + "\n";
        text += "LIGHT_GREY@@     Life: @@WHITE@@" + currentLife + "/" + life + "\n";
        text += "LIGHT_GREY@@     Light radius: @@WHITE@@" + lightRadius + "m\n\n";

        text += "BLUE_PALE@@Destiny: @@WHITE@@" + destiny + "\n";
        text += "LIGHT_GREY@@     Chance to magic find: @@WHITE@@+ "
                + (MAGIC.getProbability() + LEGENDARY.getProbability() + chanceMagicFind) + "%\n";
        text += "LIGHT_GREY@@     Chance to pick a lock: @@WHITE@@" + chanceLockPicking + "%\n";
        text += "LIGHT_GREY@@     Chance of critical hit: @@WHITE@@" + chanceCriticalHit + "%\n\n";

        return text;
    }

    public abstract String getRaceName();

    public abstract int getStrengthMin();

    public abstract int getAgilityMin();

    public abstract int getSpiritMin();

    public abstract int getDestinyMin();

    public abstract int getChanceHitMin();

    public abstract int getLifeMin();

    public abstract int getLightRadiusMin();

    public abstract int getChanceMagicFindMin();

    public abstract int getChanceLockPickingMin();

    public abstract int getChanceCriticalHitMin();

    public abstract int getInventoryLimitMin();
}
