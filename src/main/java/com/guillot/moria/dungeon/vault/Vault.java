package com.guillot.moria.dungeon.vault;

import static com.guillot.moria.configs.VaultConfig.VAULT_LIMIT;

import java.util.ArrayList;

import com.guillot.moria.item.AbstractItem;

public class Vault {

    private ArrayList<AbstractItem> items;

    public Vault() {
        items = new ArrayList<>();
    }

    public ArrayList<AbstractItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<AbstractItem> items) {
        this.items = items;
    }

    public boolean addItem(AbstractItem item) {
        if (item != null && items.size() < VAULT_LIMIT) {
            items.add(item);
            return true;
        }

        return false;
    }

}
