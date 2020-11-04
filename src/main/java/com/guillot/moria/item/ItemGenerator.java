package com.guillot.moria.item;


import static com.guillot.moria.item.ItemRarity.LEGENDARY;
import static com.guillot.moria.item.ItemRarity.MAGIC;
import static com.guillot.moria.item.ItemRarity.NORMAL;
import static com.guillot.moria.item.affixe.AbstractAffixe.AFFIXES;

import java.util.ArrayList;

import com.guillot.moria.item.affixe.AbstractAffixe;
import com.guillot.moria.item.affixe.AffixeRarity;
import com.guillot.moria.utils.RNG;
import com.guillot.moria.utils.RandomCollection;


public class ItemGenerator {

    private static ItemRarity pickRarity(int magicBonus) {
        RandomCollection<ItemRarity> random = new RandomCollection<>();

        for (ItemRarity rarity : ItemRarity.values()) {
            if (rarity.equals(NORMAL)) {
                random.add(rarity.getProbability() - magicBonus, rarity);
            } else if (rarity.equals(MAGIC)) {
                random.add(rarity.getProbability() + magicBonus, rarity);
            } else {
                random.add(rarity.getProbability(), rarity);
            }
        }

        return random.next();
    }

    private static ItemType pickType() {
        return ItemType.values()[RNG.get().randomNumberBetween(0, ItemType.values().length - 1)];
    }

    private static boolean filterAffixes(AbstractAffixe item, ItemType type) {
        return item.allowItemType(type);
    }

    private static ArrayList<AbstractAffixe> generateAffixes(ItemType type, ItemRarity rarity, int qualityLevel) {
        ArrayList<AbstractAffixe> attributes = new ArrayList<>();
        int points = rarity.getPoints();
        boolean isLegendary = false;
        int tries = 0;

        while (points > 0 && tries < 100) {
            if (rarity.equals(LEGENDARY) && !isLegendary) {
                Object[] affixes =
                        AFFIXES.stream().filter(x -> x.getType().equals(AffixeRarity.LEGENDARY) && filterAffixes(x, type)).toArray();

                AbstractAffixe affixe = (AbstractAffixe) affixes[RNG.get().randomNumberBetween(0, affixes.length - 1)];
                if (!attributes.contains(affixe) && affixe.allowItemType(type)) {
                    isLegendary = true;
                    affixe.init(qualityLevel);
                    attributes.add(affixe);
                    points -= affixe.getCost();
                }
            } else {
                Object[] affixes = AFFIXES.stream().filter(x -> filterAffixes(x, type)).toArray();

                AbstractAffixe affixe = (AbstractAffixe) affixes[RNG.get().randomNumberBetween(0, affixes.length - 1)];
                if (points - affixe.getCost() >= 0 && !attributes.contains(affixe) && affixe.allowItemType(type)) {
                    affixe.init(qualityLevel);
                    attributes.add(affixe);
                    points -= affixe.getCost();
                }
            }

            tries++;
        }

        return attributes;
    }

    public static AbstractItem generateItem(int magicBonus, int qualityLevel) {
        ItemRarity rarity = pickRarity(magicBonus);
        ItemType type = pickType();

        ArrayList<AbstractAffixe> attributes = generateAffixes(type, rarity, qualityLevel);
        AbstractItem item = null;

        switch (type) {
        case RING:
            item = new Ring();
            break;
        case AMULET:
            item = new Amulet();
            break;
        case SHIELD:
            item = new Shield();
            break;
        case ARMOR:
            item = new Armor();
            break;
        case HELMET:
            item = new Helmet();
            break;
        case STAFF:
            item = new Staff();
            break;
        case BOW:
            item = new Bow();
            break;
        case ONE_HANDED_WEAPON:
            item = new OneHandedWeapon();
            break;
        case TWO_HANDED_WEAPON:
            item = new TwoHandedWeapon();
            break;
        case HEALTH_POTION:
            item = new HealthPotion();
            break;
        case GOLD:
            // Cannot be generated
        default:
            break;
        }

        if (item == null) {
            return generateItem(magicBonus, qualityLevel);
        }

        item.setRarity(rarity);
        item.setAffixes(attributes);
        item.setQualityLevel(qualityLevel);
        item.generateBase();

        return item.isEligible() ? item : generateItem(magicBonus, qualityLevel);
    }
}
