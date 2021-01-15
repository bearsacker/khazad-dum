package com.guillot.moria.dungeon.entity;

import static com.guillot.moria.item.ItemGenerator.generateItem;
import static com.guillot.moria.item.ItemType.AMULET;
import static com.guillot.moria.item.ItemType.ARMOR;
import static com.guillot.moria.item.ItemType.BOW;
import static com.guillot.moria.item.ItemType.HEALTH_POTION;
import static com.guillot.moria.item.ItemType.HELMET;
import static com.guillot.moria.item.ItemType.ONE_HANDED_WEAPON;
import static com.guillot.moria.item.ItemType.RING;
import static com.guillot.moria.item.ItemType.SHIELD;
import static com.guillot.moria.item.ItemType.STAFF;
import static com.guillot.moria.item.ItemType.TWO_HANDED_WEAPON;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.configs.EngineConfig;
import com.guillot.moria.item.AbstractItem;
import com.guillot.moria.resources.Images;
import com.guillot.moria.utils.Point;
import com.guillot.moria.utils.RNG;
import com.guillot.moria.views.GameState;
import com.guillot.moria.views.GameView;


public class Merchant extends AbstractEntity {

    private static final long serialVersionUID = -7784617476780873995L;

    public static final int INVENTORY_LIMIT = 15;

    public static final List<MerchantRepresentation> MERCHANTS = asList(
            new MerchantRepresentation("Apothecary", 0, 0, 8, 16, 0, asList(HEALTH_POTION)), //
            new MerchantRepresentation("Armorer", 50, 100, 4, 8, 0, asList(ARMOR, SHIELD, HELMET)), //
            new MerchantRepresentation("Great Armorer", 100, 200, 4, 8, 25, asList(ARMOR, SHIELD, HELMET)), //
            new MerchantRepresentation("Weaponsmith", 50, 100, 4, 8, 0, asList(ONE_HANDED_WEAPON, TWO_HANDED_WEAPON, STAFF, BOW)), //
            new MerchantRepresentation("Great Weaponsmith", 100, 200, 4, 8, 25,
                    asList(ONE_HANDED_WEAPON, TWO_HANDED_WEAPON, STAFF, BOW)), //
            new MerchantRepresentation("Lost minor", 500, 1000, 0, 0, 0, new ArrayList<>()), //
            new MerchantRepresentation("Mysterious merchant", 0, 0, 8, 8, 95,
                    asList(ARMOR, SHIELD, HELMET, AMULET, RING, ONE_HANDED_WEAPON, TWO_HANDED_WEAPON, STAFF, BOW)), //
            new MerchantRepresentation("Jeweler", 0, 0, 5, 5, 95, asList(AMULET, RING)));

    private ArrayList<AbstractItem> inventory;

    private String name;

    private float margin;

    private int coins;

    private int frame;

    public Merchant(Point position, int level, int representationId) {
        super(position);

        type = Entity.MERCHANT;
        image = Images.MERCHANT;
        frame = representationId;

        MerchantRepresentation representation = MERCHANTS.get(representationId);
        name = representation.getName();
        margin = RNG.get().random();
        coins = RNG.get().randomNumberBetween(representation.getMinCoins(), representation.getMaxCoins());
        inventory = new ArrayList<>();

        int numberItems = RNG.get().randomNumberBetween(representation.getMinItems(), representation.getMaxItems());
        for (int i = 0; i < numberItems; i++) {
            int levelItem = level + RNG.get().randomNumberNormalDistribution(1, 1);
            AbstractItem item = generateItem(representation.getItemTypes(), representation.getMagicBonus(), levelItem);
            while (item == null) {
                item = generateItem(representation.getItemTypes(), representation.getMagicBonus(), levelItem);
            }

            inventory.add(item);
        }

        Collections.sort(inventory);
    }

    public Merchant(Point position, int level) {
        this(position, level, RNG.get().randomNumberBetween(0, MERCHANTS.size() - 1));
    }

    @Override
    public void draw(Graphics g, Point playerPosition, Color filter) {
        int x = (position.y - playerPosition.y) * 32 + (position.x - playerPosition.x) * 32 + EngineConfig.WIDTH / 2 - 32;
        int y = (position.x - playerPosition.x) * 16 - (position.y - playerPosition.y) * 16 + EngineConfig.HEIGHT / 2 - 48;

        g.drawImage(image.getSubImage(frame, 0), x, y, filter);
    }

    @Override
    public void use(GameState game, GameView view) {
        view.getTradingDialog().setMerchant(this);
        view.getTradingDialog().setVisible(true);
    }

    public ArrayList<AbstractItem> getInventory() {
        return inventory;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getName() {
        return name;
    }

    public float getMargin() {
        return 1f + margin;
    }

    public boolean isInventoryFull() {
        return inventory.size() == INVENTORY_LIMIT;
    }

    @Override
    public boolean isUsable() {
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

}
