package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.ONE_HANDED_WEAPON;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.affixe.AbstractAffixe;
import com.guillot.moria.item.affixe.AffixeFireDamage;
import com.guillot.moria.item.affixe.AffixeFrostDamage;
import com.guillot.moria.item.affixe.AffixeLightningDamage;
import com.guillot.moria.ressources.Images;

public class OneHandedWeapon extends AbstractItem implements Equipable {

    private static final long serialVersionUID = -2429016419688158012L;

    public OneHandedWeapon() {
        type = ONE_HANDED_WEAPON;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("Dagger", 1, 1, 4, 0, Images.DAGGER), //
                new ItemRepresentation("Small Axe", 2, 2, 10, 0, Images.SMALL_AXE), //
                new ItemRepresentation("Axe", 4, 4, 12, 22, Images.AXE), //
                new ItemRepresentation("Large Axe", 6, 6, 16, 30, Images.LARGE_AXE), //
                new ItemRepresentation("Short Sword", 1, 2, 6, 18, Images.SHORT_SWORD), //
                new ItemRepresentation("Sabre", 1, 1, 8, 17, Images.SABRE), //
                new ItemRepresentation("Scimitar", 4, 3, 7, 23, Images.SCIMITAR), //
                new ItemRepresentation("Blade", 4, 3, 8, 25, Images.BLADE), //
                new ItemRepresentation("Falchion", 2, 4, 8, 30, Images.FALCHION), //
                new ItemRepresentation("Long Sword", 6, 2, 10, 30, Images.LONG_SWORD), //
                new ItemRepresentation("Claymore", 5, 1, 12, 35, Images.CLAYMORE), //
                new ItemRepresentation("Broad Sword", 8, 4, 12, 40, Images.BROAD_SWORD), //
                new ItemRepresentation("Bastard Sword", 10, 6, 15, 50, Images.BASTARD_SWORD), //
                new ItemRepresentation("Club", 1, 1, 6, 0, Images.CLUB), //
                new ItemRepresentation("Spiked Club", 4, 3, 6, 18, Images.SPIKED_CLUB), //
                new ItemRepresentation("Mace", 2, 1, 8, 16, Images.MACE), //
                new ItemRepresentation("Morning Star", 3, 1, 10, 26, Images.MORNING_STAR), //
                new ItemRepresentation("Flail", 7, 2, 12, 30, Images.FLAIL));
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
