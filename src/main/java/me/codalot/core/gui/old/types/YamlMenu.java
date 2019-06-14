package me.codalot.core.gui.old.types;

import me.codalot.core.CodalotPlugin;
import me.codalot.core.files.YamlFile;
import me.codalot.core.gui.old.Menu;
import me.codalot.core.gui.old.components.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@SuppressWarnings({"WeakerAccess", "unused"})
public class YamlMenu extends Menu {

    protected YamlFile file;

    private Map<String, ItemStack> items;
    private Map<String, BiConsumer<? super Player, ? super ClickType>> actions;

    public YamlMenu(CodalotPlugin plugin, Player player, YamlFile file) {
        super(plugin, player, file.getColoredString("title"), file.getInt("rows"));
        this.file = file;

        actions = new HashMap<>();
        actions.put("exit", this::exit);
    }

    public void load() {
        loadItems();
        loadButtons();

        update();
    }

    @SuppressWarnings("all")
    protected void loadItems() {
        items = new HashMap<>();

        if (!file.contains("items"))
            return;

        file.getConfigurationSection("items").getKeys(false).forEach(key -> items.put(key, file.getItemStack("items." + key)));
    }

    protected void loadButtons() {
        if (!file.contains("buttons"))
            return;

        for (Map<?, ?> map : file.getMapList("buttons")) {
            loadButton(map);
        }
    }

    @SuppressWarnings("all")
    protected int loadButton(Map<?, ?> map) {
        String[] position = ((String) map.get("position")).replace(" ", "").split(",");
        int x = Integer.valueOf(position[0]);
        int y = Integer.valueOf(position[1]);

        ItemStack item = items.get(map.get("item"));
        BiConsumer<? super Player, ? super ClickType> action = actions.get(map.get("action"));

        setButton(x, y, new Button(item, action));

        return coordsToSlot(x, y);
    }

    public void addAction(String key, BiConsumer<? super Player, ? super ClickType> action) {
        actions.put(key, action);
    }

    private void exit(Player clicker, ClickType type) {
        player.closeInventory();
    }

}
