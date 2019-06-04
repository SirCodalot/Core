package me.codalot.core.setup.recipes;

import me.codalot.core.files.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Objects;

public class YamlRecipe extends ShapedRecipe {

    private YamlFile file;

    public YamlRecipe(NamespacedKey nsKey, ItemStack result, YamlFile file) {
        super(nsKey, result);
        this.file = file;

        shape(file.getStringList("shape").toArray(new String[0]));

        for (String key : file.getConfigurationSection("ingredients").getKeys(false)) {
            setIngredient(key.charAt(0), Objects.requireNonNull(Material.matchMaterial(Objects.requireNonNull(file.getString("ingredients." + key)))));
        }
    }

    public void add() {
        Bukkit.getServer().addRecipe(this);
    }

    public void remove() {
        new YamlRecipe(NamespacedKey.randomKey(), new ItemStack(Material.AIR), file).add();
    }

}
