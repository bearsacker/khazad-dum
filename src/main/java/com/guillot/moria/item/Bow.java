package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.BOW;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.ressources.Images;

public class Bow extends AbstractItem implements Equipable {

    private static final long serialVersionUID = 4724291481417288906L;

    public Bow() {
        type = BOW;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Short Bow", 1, 1, 4, 0, Images.BOW), //
                new ItemRepresentation("Long Bow", 5, 1, 6, 30, Images.BOW), //
                new ItemRepresentation("Hunter's Bow", 3, 2, 5, 35, Images.BOW), //
                new ItemRepresentation("Composite Bow", 7, 3, 6, 40, Images.BOW), //
                new ItemRepresentation("Short Battle Bow", 9, 3, 7, 50, Images.BOW), //
                new ItemRepresentation("Long Battle Bow", 11, 1, 10, 60, Images.BOW), //
                new ItemRepresentation("Short War Bow", 15, 4, 8, 70, Images.BOW), //
                new ItemRepresentation("Long War Bow", 19, 1, 14, 80, Images.BOW));
    }

    @Override
    public String getValueName() {
        return "Damage";
    }

    @Override
    public String getRequirementName() {
        return "Dexterity";
    }

    @Override
    public boolean isEquipable(AbstractCharacter character) {
        return character.getAgility() >= requirement;
    }

    @Override
    public void equip(AbstractCharacter character) {
        character.setDamages(character.getDamages() + value);
        setAffixesPassiveEffects(character);
    }
}
