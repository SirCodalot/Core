package me.codalot.core.managers.types;

import me.codalot.core.listeners.CodalotListener;
import me.codalot.core.CodalotPlugin;
import me.codalot.core.managers.Manager;

@SuppressWarnings("unused")
public class ListenerManager implements Manager {

    public ListenerManager(CodalotPlugin plugin, CodalotListener... listeners) {
        for (CodalotListener listener : listeners) {
            listener.register(plugin);
        }
    }

    @Override
    public void load() {

    }

    @Override
    public void save() {

    }

}
