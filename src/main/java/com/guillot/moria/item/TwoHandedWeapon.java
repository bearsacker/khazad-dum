package com.guillot.moria.item;

import static com.guillot.moria.item.ItemType.TWO_HANDED_WEAPON;

import java.util.Arrays;
import java.util.List;

import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.item.affixe.AbstractAffixe;
import com.guillot.moria.item.affixe.AffixeFireDamage;
import com.guillot.moria.item.affixe.AffixeFrostDamage;
import com.guillot.moria.item.affixe.AffixeLightningDamage;
import com.guillot.moria.ressources.Images;

public class TwoHandedWeapon extends AbstractItem implements Equipable {

    private static final long serialVersionUID = 854933691169367108L;

    public TwoHandedWeapon() {
        type = TWO_HANDED_WEAPON;
    }

    @Override
    public List<ItemRepresentation> getValuesPerLevel() {
        return Arrays.asList(
                new ItemRepresentation("War Hammer", 5, 5, 9, 40, Images.WAR_HAMMER), //
                new ItemRepresentation("Broad Axe", 8, 8, 20, 50, Images.BROAD_AXE), //
                new ItemRepresentation("Maul", 10, 6, 20, 55, Images.MAUL), //
                new ItemRepresentation("Battle Axe", 10, 10, 25, 65, Images.BATTLE_AXE), //
                new ItemRepresentation("Great Axe", 12, 12, 30, 80, Images.GREAT_AXE), //
                new ItemRepresentation("Two-Handed Sword", 14, 8, 16, 65, Images.TWO_HANDED_SWORD), //
                new ItemRepresentation("Great Sword", 17, 10, 20, 75, Images.GREAT_SWORD));
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
