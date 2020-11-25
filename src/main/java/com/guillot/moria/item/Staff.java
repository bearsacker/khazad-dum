package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.STAFF;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.affixe.AbstractAffixe;
import com.guillot.moria.item.affixe.AffixeFireDamage;
import com.guillot.moria.item.affixe.AffixeFrostDamage;
import com.guillot.moria.item.affixe.AffixeLightningDamage;
import com.guillot.moria.ressources.Images;

public class Staff extends AbstractItem implements Equipable {

    private static final long serialVersionUID = 3076341665961717631L;

    public Staff() {
        type = STAFF;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Short Staff", 1, 2, 4, 0, Images.SHORT_STAFF), //
                new ItemRepresentation("Long Staff", 4, 4, 8, 0, Images.LONG_STAFF), //
                new ItemRepresentation("Composite Staff", 6, 5, 10, 15, Images.COMPOSITE_STAFF), //
                new ItemRepresentation("Quarter Staff", 9, 6, 12, 20, Images.QUARTER_STAFF), //
                new ItemRepresentation("War Staff", 12, 8, 16, 30, Images.WAR_STAFF));
    }

    @Override
    public String getValueName() {
        return "Damage";
    }

    @Override
    public String getRequirementName() {
        return "Strength";
    }

    @Override
    public boolean isEquipable(AbstractCharacter character) {
        return character.getStrength() >= requirement;
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
