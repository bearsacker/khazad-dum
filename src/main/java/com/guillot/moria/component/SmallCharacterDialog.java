package com.guillot.moria.component;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.GUI;
import com.guillot.engine.gui.ProgressBar;
import com.guillot.engine.gui.Text;
import com.guillot.engine.gui.Window;
import com.guillot.moria.character.AbstractCharacter;
import com.guillot.moria.ressources.Images;
import com.guillot.moria.views.GameView;

public class SmallCharacterDialog extends Window {

    private final static Color NAME_COLOR = new Color(223, 207, 134);

    private final static Color OTHER_COLOR = new Color(240, 220, 220);

    private AbstractCharacter character;

    private ProgressBar lifeBar;

    private Text nameText;

    private Text damagesText;

    private Text armorText;

    private Text chanceHitText;

    private Text chanceCriticalHitText;

    private Text chanceDodge;

    private SmallInventoryComponent inventory;

    public SmallCharacterDialog(GameView parent) throws Exception {
        super(null, 0, 0, 384, 312);

        showOverlay = false;

        nameText = new Text("", 32, 72, GUI.get().getFont(1), NAME_COLOR);

        lifeBar = new ProgressBar(32, 104, width - 64, 16, 0);

        damagesText = new Text("", 80, 136, GUI.get().getFont(2), Color.white);
        armorText = new Text("", 264, 136, GUI.get().getFont(2), Color.white);

        chanceHitText = new Text("", 40, 176, GUI.get().getFont(), OTHER_COLOR);
        chanceCriticalHitText = new Text("", 40, 200, GUI.get().getFont(), OTHER_COLOR);
        chanceDodge = new Text("", 224, 176, GUI.get().getFont(), OTHER_COLOR);

        inventory = new SmallInventoryComponent(36, 240);

        add(nameText, lifeBar, damagesText, armorText, chanceHitText, chanceCriticalHitText, chanceDodge, inventory);
    }

    @Override
    public void onShow() throws Exception {
        if (character != null) {
            nameText.setText(character.getName() + " - Level " + character.getLevel());

            lifeBar.setValue(character.getCurrentLife() / (float) character.getLife());

            damagesText.setText(character.computeMinDamages() + "-" + character.computeMaxDamages());
            armorText.setText(Integer.toString(character.getArmor()));

            chanceHitText.setText(character.getChanceHit() + "% hit");
            chanceCriticalHitText.setText(character.getChanceCriticalHit() + "% critical hit");
            chanceDodge.setText(character.getChanceDodge() + "% dodge");

            inventory.setItems(character);
        }
    }

    @Override
    public void update() throws Exception {
        super.update();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.pushTransform();
        g.translate(x, y);

        g.drawImage(Images.SWORD.getImage(), 40, 136, 40 + 32, 136 + 32, 0, 0, 16, 16, filter);
        g.drawImage(Images.LARGE_SHIELD.getImage(), 224, 136, 224 + 32, 136 + 32, 0, 0, 16, 16, filter);

        g.popTransform();
    }

    public void setCharacter(AbstractCharacter character) {
        this.character = character;
    }

}
