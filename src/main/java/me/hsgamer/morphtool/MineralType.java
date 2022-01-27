package me.hsgamer.morphtool;

import org.bukkit.Material;

public enum MineralType {
    DIAMOND("DIAMOND_"),
    GOLDEN("GOLDEN_"),
    IRON("IRON_"),
    STONE("STONE_"),
    WOODEN("WOODEN_"),
    NETHERITE("NETHERITE_"),
    UNKNOWN(null);
    private final String startName;

    MineralType(String startName) {
        this.startName = startName;
    }

    public static MineralType getMineralType(Material material) {
        String name = material.name();
        for (MineralType mineralType : values()) {
            if (mineralType.startName == null) continue;
            if (name.startsWith(mineralType.startName)) {
                return mineralType;
            }
        }
        return UNKNOWN;
    }

    public String getStartName() {
        return startName;
    }

    public Material getMaterial(String type) {
        return Material.getMaterial(startName + type);
    }
}
