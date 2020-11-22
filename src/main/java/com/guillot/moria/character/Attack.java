package com.guillot.moria.character;


public class Attack {

    private int damages;

    private int fireDamages;

    private int frostDamages;

    private int lightningDamages;

    private boolean critical;

    public Attack(int damages, int fireDamages, int frostDamages, int lightningDamages, boolean critical) {
        this.damages = damages;
        this.fireDamages = fireDamages;
        this.frostDamages = frostDamages;
        this.lightningDamages = lightningDamages;
        this.critical = critical;
    }

    public int getDamages() {
        return damages;
    }

    public void setDamages(int damages) {
        this.damages = damages;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public int getFireDamages() {
        return fireDamages;
    }

    public void setFireDamages(int fireDamages) {
        this.fireDamages = fireDamages;
    }

    public int getFrostDamages() {
        return frostDamages;
    }

    public void setFrostDamages(int frostDamages) {
        this.frostDamages = frostDamages;
    }

    public int getLightningDamages() {
        return lightningDamages;
    }

    public void setLightningDamages(int lightningDamages) {
        this.lightningDamages = lightningDamages;
    }

}
