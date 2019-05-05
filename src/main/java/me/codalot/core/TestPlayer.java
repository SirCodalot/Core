package me.codalot.core;

import me.codalot.core.files.YamlFile;
import me.codalot.core.player.CPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestPlayer extends CPlayer {

    public TestPlayer(UUID uuid, YamlFile file) {
        super(uuid, file);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("hello", "world");
        return map;
    }

}
