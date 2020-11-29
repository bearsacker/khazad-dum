package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.BOW;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.affixe.AbstractAffixe;
import com.guillot.moria.item.affixe.AffixeFireDamage;
import com.guillot.moria.item.affixe.AffixeFrostDamage;
import com.guillot.moria.item.affixe.AffixeLightningDamage;
import com.guillot.moria.ressources.Images;

public class Bow extends AbstractItem implements Equipable {

    private static final long serialVersionUID = 4724291481417288906L;

    public Bow() {
        type = BOW;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Short Bow", 1, 1, 4, 0, Images.SHORT_BOW), //
                new ItemRepresentation("Long Bow", 5, 1, 6, 30, Images.LONG_BOW), //
                new ItemRepresentation("Hunter's Bow", 3, 2, 5, 35, Images.HUNTER_BOW), //
                new ItemRepresentation("Composite Bow", 7, 3, 6, 40, Images.COMPOSITE_BOW), //
                new ItemRepresentation("Short Battle Bow", 9, 3, 7, 50, Images.SHORT_BATTLE_BOW), //
                new ItemRepresentation("Long Battle Bow", 11, 1, 10, 60, Images.LONG_BATTLE_BOW), //
                new ItemRepresentation("Short War Bow", 15, 4, 8, 70, Images.SHORT_WAR_BOW), //
                new ItemRepresentation("Long War Bow", 19, 1, 14, 80, Images.LONG_WAR_BOW));
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

    @Override
    public boolean isEligible() {
        int numberAffixesDamage = 0;
        for (AbstractAffixe affixe : affixes) {
            if (affixe instanceof AffixeFireDamage || affixe instanceof AffixeFrostDamage || affixe instanceof AffixeLightningDamage) {
                numberAffixesDamage++;
            }
        }

        return super.isEligible() && numberAffixesDamage <= 1;
    }
}
