package com.guillot.moria.dungeon.vault;

import static com.guillot.moria.configs.VaultConfig.VAULT_LIMIT;

import java.util.ArrayList;

import com.guillot.moria.item.AbstractItem;

public class Vault {

    private ArrayList<AbstractItem> items;

    private int limit;

    public Vault() {
        items = new ArrayList<>();
        limit = VAULT_LIMIT;
    }

    public ArrayList<AbstractItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<AbstractItem> items) {
        this.items = items;
    }

    public boolean addItem(AbstractItem item) {
        if (item != null && items.size() < limit) {
            items.add(item);
            return true;
        }

        return false;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

}
