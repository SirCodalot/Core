package me.codalot.core.gui;

import lombok.Getter;
import lombok.Setter;
import me.codalot.core.CodalotPlugin;
import me.codalot.core.files.YamlFile;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuppressWarnings("WeakerAccess")
public class YamlScroll extends YamlMenu implements Scroll {

    private int page;
    private int scrollWidth;

    @Setter private List<Button> list;
    private List<Integer> slots;

    public YamlScroll(CodalotPlugin plugin, Player player, YamlFile file) {
        super(plugin, player, file);

        page = 0;
        scrollWidth = file.getInt("scroll_width", 1);

        slots = new ArrayList<>();
        file.getStringList("list_positions").forEach(position -> slots.add(coordsToSlot(position)));

        addAction("prev_page", (p, t) -> prevPage());
        addAction("next_page", (p, t) -> nextPage());
        addAction("first_page", (p, t) -> setPage(0));
        addAction("last_page", (p, t) -> setPage(getPages()));
    }

    @Override
    protected void update() {
        updatePage();
        updateScroll();

        super.update();
    }

    protected void updateScroll() {
        slots.forEach(this::removeButton);

        for (int i = 0; i < getPageSize(); i++) {
            try {
                int slot = slots.get(i);
                Button button = list.get(i + page * scrollWidth);

                setButton(slot, button);
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
    }

    private void updatePage() {
        if (page < 0)
            page = 0;
        if (page > getPages())
            page = getPages();
    }

    @Override
    public int getPages() {
        int pages = 0;
        int items = list.size();

        while (items > 0) {
            pages++;
            items -= scrollWidth;
        }

        return pages <= 0 ? 0 : pages - 1;
    }

    @Override
    public int getPageSize() {
        return slots.size();
    }

    @Override
    public void setPage(int page) {
        this.page = page;
        updatePage();
    }

    @Override
    public void nextPage() {
        setPage(page + 1);
    }

    @Override
    public void prevPage() {
        setPage(page - 1);
    }
}
