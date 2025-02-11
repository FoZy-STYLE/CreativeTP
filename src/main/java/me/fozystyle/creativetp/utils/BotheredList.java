package me.fozystyle.creativetp.utils;

import java.util.concurrent.ConcurrentHashMap;

public class BotheredList {
    private final ConcurrentHashMap<String, Long> botheredPlayers = new ConcurrentHashMap<>();

    public BotheredList(String botheredPlayerUUID) {
        addBotheredPlayer(botheredPlayerUUID);
    }

    public void addBotheredPlayer(String playerUUID) {
        botheredPlayers.put(playerUUID, System.currentTimeMillis());
    }

    public boolean hasPlayer(String playerUUID) {
        return botheredPlayers.containsKey(playerUUID);
    }

    public int getTimeDifference(String playerUUID) {
        return (int) ((System.currentTimeMillis() - botheredPlayers.get(playerUUID)) * 0.001);
    }

    public boolean isBothered(String playerUUID) {
        return getTimeDifference(playerUUID) < Config.globalConfig.getCooldown();
    }

    public void resetPlayerCooldown(String playerUUID) {
        addBotheredPlayer(playerUUID);
    }
}

