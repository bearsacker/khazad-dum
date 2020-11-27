package com.guillot.moria.save;

import static com.guillot.moria.configs.VaultConfig.VAULT_VERSION;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.guillot.moria.dungeon.vault.Vault;
import com.guillot.moria.item.AbstractItem;

public class VaultSaveManager {

    public final static String VAULT_SAVE_PATH = "vault.dat";

    public static boolean isSaveFilePresent(String path) {
        return new File(path).exists();
    }

    public static void writeSaveFile(String path, Vault vault) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream stream = new ObjectOutputStream(fos);

        stream.writeInt(VAULT_VERSION);
        stream.writeInt(vault.getItems().size());
        for (AbstractItem item : vault.getItems()) {
            stream.writeObject(item);
        }

        stream.close();
        fos.close();
    }

    public static Vault loadSaveFile(String path) throws IOException, ClassNotFoundException {
        File file = new File(path);
        if (!file.exists()) {
            return new Vault();
        }

        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream stream = new ObjectInputStream(fis);

        Vault vault = new Vault();

        int version = stream.readInt();
        if (version == VAULT_VERSION) {
            int itemsNumber = stream.readInt();
            for (int i = 0; i < itemsNumber; i++) {
                vault.getItems().add((AbstractItem) stream.readObject());
            }
        }

        stream.close();
        fis.close();

        return vault;
    }
}
