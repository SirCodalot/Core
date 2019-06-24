package me.codalot.core.utils;

import org.bukkit.Material;

public class MaterialUtils {

    public static boolean isAir(Material material) {
        switch (material) {
            case LEGACY_AIR:
            case AIR:
            case CAVE_AIR:
            case VOID_AIR:
                return true;
            default:
                return false;
        }
    }

    public static Material getMaterial(String name) {
        try {
            return (Material) Material.class.getDeclaredField(name).get(null);
        } catch (Exception ignored) {}

        return Material.STONE;
    }

}
