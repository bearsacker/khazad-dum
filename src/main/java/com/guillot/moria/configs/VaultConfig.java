package com.guillot.moria.configs;

import org.apache.commons.configuration2.Configuration;

import com.guillot.engine.configs.Config;

public class VaultConfig extends Config {

    private static final VaultConfig INSTANCE = new VaultConfig("vault.properties");

    private VaultConfig(String file) {
        super(file);
    }

    public static Configuration get() {
        return INSTANCE.configuration;
    }

    public static final int VAULT_VERSION = get().getInt("vault.version");

    public static final int VAULT_LIMIT = get().getInt("vault.limit");

}
