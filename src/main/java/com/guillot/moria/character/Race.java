package com.guillot.moria.character;


public enum Race {
    HUMAN("Human"), ELF("Elf"), HOBBIT("Hobbit"), DWARF("Dwarf");

    private String name;

    private Race(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
