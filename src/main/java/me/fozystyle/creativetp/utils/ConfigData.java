package me.fozystyle.creativetp.utils;
import java.util.ArrayList;
import java.util.List;

public class ConfigData {
    private final List<String> dnd;
    private final int cooldown;
    private final List<String> configurableMessages;


    public List<String> getDndList() {
        return dnd;
    }

    public List<String> getConfigurableMessages() {
        return configurableMessages;
    }

    public int getCooldown() {
        return cooldown;
    }

    public ConfigData(List<String> dnd, int cooldown, List<String> configurableMessages) {
        this.dnd = new ArrayList<>(dnd);
        this.configurableMessages = new ArrayList<>(configurableMessages);
        this.cooldown = cooldown;
    }
}