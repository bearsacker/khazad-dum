package com.guillot.moria.character;

import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.utils.Point;

public abstract class AbstractCharacter {

    protected String name;

    protected int level;

    protected int xp;

    protected int currentLife;

    // Attributes

    protected int strength;

    protected int agility;

    protected int spirit;

    protected int destiny;

    // Computed from strength

    protected int physicalDamage;

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

    // Computed from equipment

    protected int fireDamage;

    protected int frostDamage;

    protected int lightningDamage;

    protected int armor;

    // Equipment

    protected int gold;

    protected AbstractItem head;

    protected AbstractItem body;

    protected AbstractItem leftHand;

    protected AbstractItem rightHand;

    protected AbstractItem twoHands;

    protected AbstractItem finger;

    protected AbstractItem neck;

    protected Point position;

    protected AbstractCharacter(String name) {
        this.name = name;

        this.level = 1;
        this.xp = 0;

        this.head = null;
        this.body = null;
        this.leftHand = null;
        this.rightHand = null;
        this.twoHands = null;
        this.finger = null;
        this.neck = null;

        this.strength = this.getStrengthMin();
        this.agility = this.getAgilityMin();
        this.spirit = this.getSpiritMin();
        this.destiny = this.getDestinyMin();

        computeStatistics();
        this.currentLife = this.life;
    }

    public String getName() {
        return this.name;
    }

    public void computeStatistics() {
        physicalDamage = strength / 3;

        chanceHit = getChanceHitMin() + agility / 2;
        chanceDodge = agility / 2;
        movement = agility / 3;

        life = getLifeMin() + (level - 1) * getLifePerLevel() + spirit * getLifePerSpirit();
        lightRadius = getLightRadiusMin() + spirit / 4;

        chanceMagicFind = getChanceMagicFindMin() + destiny;
        chanceLockPicking = getChanceLockPickingMin() + destiny;
    }

    public void equipItem(AbstractItem item) {
        if (item != null && item.isEquipable(this)) {
            switch (item.getBlock()) {
            case HEAD:
                if (this.head != null) {
                    this.unequipItem(this.head);
                }
                this.head = item;
                break;
            case BODY:
                if (this.body != null) {
                    this.unequipItem(this.body);
                }
                this.body = item;
                break;
            case LEFT_HAND:
                if (this.twoHands != null) {
                    this.unequipItem(this.twoHands);
                }
                if (this.leftHand != null) {
                    this.unequipItem(this.leftHand);
                }
                this.leftHand = item;
                break;
            case RIGHT_HAND:
                if (this.twoHands != null) {
                    this.unequipItem(this.twoHands);
                }
                if (this.rightHand != null) {
                    this.unequipItem(this.rightHand);
                }
                this.rightHand = item;
                break;
            case TWO_HANDS:
                if (this.leftHand != null) {
                    this.unequipItem(this.leftHand);
                }
                if (this.rightHand != null) {
                    this.unequipItem(this.rightHand);
                }
                this.twoHands = item;
                break;
            case FINGER:
                if (this.finger != null) {
                    this.unequipItem(this.finger);
                }
                this.finger = item;
                break;
            case NECK:
                if (this.neck != null) {
                    this.unequipItem(this.neck);
                }
                this.neck = item;
                break;
            default:
                break;
            }

            item.setPassiveEffect(this);
            this.computeStatistics();
        }
    }

    public void unequipItem(AbstractItem item) {
        item.unsetPassiveEffect(this);
        item = null;
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
        if (currentLife > this.life) {
            currentLife = this.life;
        }

        this.currentLife = currentLife;
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
        this.chanceHit = chanceHit;
    }

    public int getChanceDodge() {
        return chanceDodge;
    }

    public void setChanceDodge(int chanceDodge) {
        this.chanceDodge = chanceDodge;
    }

    public int getChanceMagicFind() {
        return chanceMagicFind;
    }

    public void setChanceMagicFind(int chanceMagicFind) {
        this.chanceMagicFind = chanceMagicFind;
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

    public AbstractItem getHead() {
        return head;
    }

    public AbstractItem getBody() {
        return body;
    }

    public AbstractItem getLeftHand() {
        return leftHand;
    }

    public AbstractItem getRightHand() {
        return rightHand;
    }

    public AbstractItem getTwoHands() {
        return twoHands;
    }

    public AbstractItem getFinger() {
        return finger;
    }

    public AbstractItem getNeck() {
        return neck;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = new Point(position);
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    @Override
    public String toString() {
        String text = this.getName() + " [lvl " + level + "]\n";
        text += "===================================\n";
        text += "Strength: " + strength + "\n";
        text += "\tPhysical damage: + 0-" + physicalDamage + "%\n";

        text += "Agility: " + agility + "\n";
        text += "\tChance to Hit: " + chanceHit + "%\n";
        text += "\tChance to Block/Dodge: " + chanceDodge + "%\n";
        text += "\tMovement: " + movement + "m\n";

        text += "Spirit: " + spirit + "\n";
        text += "\tLife: " + currentLife + "/" + life + "\n";
        text += "\tLight radius: " + lightRadius + "m\n";

        text += "Destiny: " + destiny + "\n";
        text += "\tChance to find a magic object: + " + chanceMagicFind + "%\n";
        text += "\tChance to pick a lock: " + chanceLockPicking + "%\n";

        text += "\n";
        text += "Armor: " + armor + "\n";
        text += "Fire damage: + " + fireDamage + "%\n";
        text += "Frost damage: + " + frostDamage + "%\n";
        text += "Lightning damage: + " + lightningDamage + "%\n";

        return text;
    }

    public abstract String getClassName();

    public abstract int getStrengthMin();

    public abstract int getAgilityMin();

    public abstract int getSpiritMin();

    public abstract int getDestinyMin();

    public abstract int getChanceHitMin();

    public abstract int getLifeMin();

    public abstract int getLifePerLevel();

    public abstract int getLifePerSpirit();

    public abstract int getLightRadiusMin();

    public abstract int getChanceMagicFindMin();

    public abstract int getChanceLockPickingMin();
}
