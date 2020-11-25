package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.BAG;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.utils.RNG;


public class Bag extends AbstractItem implements Usable {

    private static final long serialVersionUID = 9073072450585182682L;

    public Bag() {
        type = BAG;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return asList(new ItemRepresentation("Mysterious bag", 1, 0, 0, 0, Images.BAG));
    }

    @Override
    public void generateBase(int qualityLevel) {
        super.generateBase(qualityLevel);
        affixes = new ArrayList<>();
        rarity = ItemRarity.NORMAL;
    }

    @Override
    public String getValueName() {
        return null;
    }

    @Override
    public boolean use(AbstractCharacter character) {
        AbstractItem item = null;
        do {
            item = ItemGenerator.generateItem(character.getChanceMagicFind(), character.getLevel() + RNG.get().randomNumber(5));
        } while (item == null || item instanceof Bag);

        if (character.dropItem(this)) {
            return character.pickUpItem(item);
        }

        return false;
    }
}
