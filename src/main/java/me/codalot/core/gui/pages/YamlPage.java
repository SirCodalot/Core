package me.codalot.core.gui.pages;

import lombok.Getter;
import me.codalot.core.files.YamlFile;
import me.codalot.core.gui.components.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.BiConsumer;

@Getter
@SuppressWarnings({"WeakerAccess", "unused"})
public class YamlPage extends Page {

    private YamlFile file;

    private Map<String, BiConsumer<Player, ClickType>> actions;

    @SuppressWarnings("all")
    public YamlPage(YamlFile file, Map<String, BiConsumer<Player, ClickType>> actions) {
        super();

        this.file = file;
        this.actions = actions == null ? new HashMap<>() : actions;

        actions.put("exit", (player, click) -> player.closeInventory());
        load();
    }

    public YamlPage(YamlFile file) {
        this(file, new HashMap<>());
    }

    public void load() {
        if (!file.contains("buttons"))
            return;

        Map<String, ItemStack> items = loadItems();
        loadButtons(items);
    }

    @SuppressWarnings("all")
    protected Map<String, ItemStack> loadItems() {
        if (!file.contains("items"))
            return new HashMap<>();

        Map<String, ItemStack> items = new HashMap<>();
        for (String key : file.getConfigurationSection("items").getKeys(false)) {
            items.put(key, file.getItem("items." + key));
        }

        return items;
    }

    @SuppressWarnings("all")
    protected void loadButtons(Map<String, ItemStack> items) {
        List<Map<String, Object>> buttons = (List<Map<String, Object>>) file.get("buttons");
        for (Map<String, Object> map : buttons) {


            Button button = new Button(items, actions, map);

            Set<String> positions = new HashSet<>();
            if (map.containsKey("positions"))
                positions.addAll((List<String>) map.get("positions"));
            if (map.containsKey("position"))
                positions.add((String) map.get("position"));

            positions.forEach(position -> put(position, button.clone()));
        }
    }

}
