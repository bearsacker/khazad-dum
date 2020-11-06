package com.guillot.moria.character;

import java.util.ArrayList;

import org.newdawn.slick.Image;

import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.item.Equipable;
import com.guillot.moria.utils.DepthBufferedImage;
import com.guillot.moria.utils.Point;

public abstract class AbstractCharacter {

    protected String name;

    protected int level;

    protected int xp;

    protected int currentLife;

    protected Point position;

    protected Image image;

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

    protected int damages;

    protected int armor;

    // Equipment

    protected int gold;

    protected ArrayList<AbstractItem> inventory;

    protected AbstractItem head;

    protected AbstractItem body;

    protected AbstractItem leftHand;

    protected AbstractItem rightHand;

    protected AbstractItem finger;

    protected AbstractItem neck;

    protected AbstractCharacter(String name, Image image) {
        this.name = name;
        this.image = image;

        this.level = 1;
        this.xp = 0;

        this.inventory = new ArrayList<>();
        this.head = null;
        this.body = null;
        this.leftHand = null;
        this.rightHand = null;
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

        life = getLifeMin() + spirit * getLifePerSpirit();
        lightRadius = getLightRadiusMin() + spirit / 4;

        chanceMagicFind = getChanceMagicFindMin() + destiny;
        chanceLockPicking = getChanceLockPickingMin() + destiny;
    }

    public boolean pickUpItem(AbstractItem item) {
        if (!inventory.contains(item)) {
            inventory.add(item);
        }

        return true;
    }

    public void equipItem(AbstractItem item) {
        if (item instanceof Equipable && ((Equipable) item).isEquipable(this)) {
            switch (item.getBlock()) {
            case HEAD:
                if (head != null) {
                    unequipItem(head);
                }
                head = item;
                break;
            case BODY:
                if (body != null) {
                    unequipItem(body);
                }
                body = item;
                break;
            case LEFT_HAND:
                if (leftHand != null) {
                    if (rightHand == leftHand) {
                        rightHand = null;
                    }

                    unequipItem(leftHand);
                }
                leftHand = item;
                break;
            case RIGHT_HAND:
                if (rightHand != null) {
                    if (rightHand == leftHand) {
                        leftHand = null;
                    }

                    unequipItem(rightHand);
                }
                rightHand = item;
                break;
            case TWO_HANDS:
                if (leftHand != null) {
                    unequipItem(leftHand);
                }
                if (rightHand != null) {
                    unequipItem(rightHand);
                }
                leftHand = item;
                rightHand = item;
                break;
            case FINGER:
                if (finger != null) {
                    unequipItem(finger);
                }
                finger = item;
                break;
            case NECK:
                if (neck != null) {
                    unequipItem(neck);
                }
                neck = item;
                break;
            default:
                break;
            }

            if (item instanceof Equipable) {
                ((Equipable) item).equip(this);
            }
            computeStatistics();
        }
    }

    public void unequipItem(AbstractItem item) {
        if (item instanceof Equipable) {
            ((Equipable) item).unequip(this);
        }
        item = null;
    }

    public boolean isEquipedByItem(AbstractItem item) {
        return head == item || neck == item || finger == item || body == item || leftHand == item || rightHand == item;
    }

    public void draw(DepthBufferedImage image) {
        image.drawImage(this.image, image.getCenterOfRotationX() - 32, image.getCenterOfRotationY() - 48);
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

    public ArrayList<AbstractItem> getInventory() {
        return inventory;
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
        String text = this.getName() + " - Level " + level + "\n\n";
        text += "Strength: " + strength + "\n";
        text += "     Physical damage: + 0-" + physicalDamage + "%\n";

        text += "Agility: " + agility + "\n";
        text += "     Chance to Hit: " + chanceHit + "%\n";
        text += "     Chance to Block/Dodge: " + chanceDodge + "%\n";
        text += "     Movement: " + movement + "m\n";

        text += "Spirit: " + spirit + "\n";
        text += "     Life: " + currentLife + "/" + life + "\n";
        text += "     Light radius: " + lightRadius + "m\n";

        text += "Destiny: " + destiny + "\n";
        text += "     Chance to find a magic object: + " + chanceMagicFind + "%\n";
        text += "     Chance to pick a lock: " + chanceLockPicking + "%\n\n";

        text += "Damages: " + damages + "\n";
        text += "Armor: " + armor + "\n";
        text += "Fire damage: + " + fireDamage + "%\n";
        text += "Frost damage: + " + frostDamage + "%\n";
        text += "Lightning damage: + " + lightningDamage + "%";

        return text;
    }

    public abstract String getClassName();

    public abstract int getStrengthMin();

    public abstract int getAgilityMin();

    public abstract int getSpiritMin();

    public abstract int getDestinyMin();

    public abstract int getChanceHitMin();

    public abstract int getLifeMin();

    public abstract int getLifePerSpirit();

    public abstract int getLightRadiusMin();

    public abstract int getChanceMagicFindMin();

    public abstract int getChanceLockPickingMin();
}
