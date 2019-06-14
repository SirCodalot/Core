package me.codalot.core.gui.data;

import lombok.Getter;
import me.codalot.core.files.YamlFile;
import me.codalot.core.gui.pages.YamlPage;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

@Getter
@SuppressWarnings({"WeakerAccess", "unused"})
public class YamlMenuData extends MenuData {

    private YamlFile file;

    public YamlMenuData(YamlFile file, Map<String, BiConsumer<Player, ClickType>> actions) {
        super(
                file.getColoredString("title", "&8Inventory"),
                file.getInt("rows", 3),
                file.getBoolean("open-previous", false),
                new YamlPage(file, actions),
                file.getString("open-sound"),
                file.getString("close-sound"),
                file.getString("switch-sound")
        );

        this.file = file;
    }

    public YamlMenuData(YamlFile file) {
        this(file, new HashMap<>());
    }


}
