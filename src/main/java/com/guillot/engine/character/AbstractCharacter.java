package com.guillot.engine.character;

import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.utils.Point;

public abstract class AbstractCharacter {

    protected String name;

    protected int level;

    protected int xp;

    protected int currentLife;

    protected int strength;

    protected int dexterity;

    protected int vitality;

    protected int life;

    protected int lightRadius;

    protected int speed;

    protected int physicalDamage;

    protected int fireDamage;

    protected int frostDamage;

    protected int lightningDamage;

    protected int chanceHit;

    protected int chanceDodge;

    protected int luck;

    protected int armor;

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
        this.dexterity = this.getDexterityMin();
        this.vitality = this.getVitalityMin();
        this.lightRadius = this.getLightRadiusMin();
        this.luck = this.getLuckMin();

        computeStatistics();
        this.currentLife = this.life;
    }

    public String getName() {
        return this.name;
    }

    public void computeStatistics() {
        this.life = 0 + (this.level - 1) * 0
                + this.vitality * 0;
        this.chanceHit = 0 + this.dexterity / 2;
        this.chanceDodge = this.dexterity / 2;
        this.speed = this.dexterity / 3;
        this.physicalDamage = this.strength / 3;
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

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getVitality() {
        return vitality;
    }

    public void setVitality(int vitality) {
        this.vitality = vitality;
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

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
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

    public int getMagicFind() {
        return luck;
    }

    public void setMagicFind(int magicFind) {
        this.luck = magicFind;
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

    @Override
    public String toString() {
        String text = this.getName() + " [lvl " + level + "]\n";
        text += "===================================\n";
        text += "Strength: " + strength + "\n";
        text += "Dexterity: " + dexterity + "\n";
        text += "Vitality: " + vitality + "\n";

        text += "Life: " + currentLife + "/" + life + "\n";

        text += "Armor: " + armor + "\n";
        text += "Speed: + " + speed + "%\n";
        text += "Physical damage: + " + physicalDamage + "%\n";
        text += "Fire damage: + " + fireDamage + "%\n";
        text += "Frost damage: + " + frostDamage + "%\n";
        text += "Lightning damage: + " + lightningDamage + "%\n";
        text += "Light radius: " + lightRadius + "m\n";
        text += "Chance to Hit: " + chanceHit + "%\n";
        text += "Chance to Block/Dodge: " + chanceDodge + "%\n";
        text += "Luck: + " + luck + "%\n";

        return text;
    }

    public abstract String getClassName();

    public abstract int getStrengthMin();

    public abstract int getDexterityMin();

    public abstract int getVitalityMin();

    public abstract int getLightRadiusMin();

    public abstract int getLuckMin();
}
