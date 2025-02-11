package me.fozystyle.creativetp.utils;

import net.fabricmc.loader.api.FabricLoader;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static me.fozystyle.creativetp.utils.Misc.LogException;


public final class Config {
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("CreativeTP.config");
    public static final ConfigData DEFAULT_CONFIG = new ConfigData(new ArrayList<>(), 10, getDefaultMessages());
    public static ConfigData globalConfig;

    public static void init() {
        loadConfig();
    }

    public static void loadConfig() {
        if (!CONFIG_PATH.toFile().exists()) {
            writeDefaultConfig();
            globalConfig = DEFAULT_CONFIG;
            return;
        }

        globalConfig = parseConfig();
    }

    public static void writeDefaultConfig() {
        try {
            saveConfig(DEFAULT_CONFIG);
        } catch (IOException e) {
            LogException("An error occurred whilst trying to write the default config file..", e);
        }
    }

    public static ConfigData parseConfig() {
        int cooldown = DEFAULT_CONFIG.getCooldown();
        List<String> dndList = DEFAULT_CONFIG.getDndList();
        List<String> messages = DEFAULT_CONFIG.getConfigurableMessages();

        String[] lines;
        try {
            lines = Files.readString(CONFIG_PATH).split("\n");
        } catch (IOException e) { LogException("An error occurred whilst trying to read the config file..", e);  return DEFAULT_CONFIG; }

        for (String line : lines) {
            if (line.startsWith("#") || line.isBlank()) continue;

            if (line.startsWith("Cooldown=")) {
                try {
                    cooldown = Integer.parseInt(line.split("=")[1]);
                } catch (Exception e) {LogException("Couldn't read cooldown value from the config..", e); cooldown = DEFAULT_CONFIG.getCooldown(); }

            } else if (line.startsWith("DND=")) {
               try {
                   dndList = List.of(line.split("=")[1].split(","));
               } catch (Exception e) {LogException("Couldn't retrieve 'do not disturb' list from the config..", e); dndList = DEFAULT_CONFIG.getDndList(); }

            } else {
                try {
                    if (!line.substring(1).startsWith("msg=")) continue;
                } catch (Exception e) { continue; }

                int index;
                try {
                    String i = line.charAt(0) + "";
                    index = Integer.parseInt(i);
                } catch (Exception e) {LogException("Couldn't retrieve message index..", e); continue; }
                try {
                    messages.set(index, line.split("=")[1]);
                } catch (Exception e) {LogException("Couldn't retrieve configurable message " + index + " from the config..", e); }

            }

        }

        return new ConfigData(dndList, cooldown, messages);
    }

    public static void saveConfig(ConfigData configData) throws IOException {
        if (!CONFIG_PATH.toFile().exists()) {
            Files.createFile(CONFIG_PATH);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Cooldown=" + configData.getCooldown());

        sb.append("\n");

        sb.append("\n# Do not disturb list (stored in UUID). Don't touch this unless you know what you are doing. \n");
        sb.append("DND=");
        for (String s : configData.getDndList()) {
            sb.append(s + ",");
        }
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append("\n");

        sb.append("\n# Configurable Messages (the prefix number serves as the message ID) \n");
        sb.append("# --------------------------------------------------------------------- \n");
        for (int i = 0; i < configData.getConfigurableMessages().size(); i++) {
            sb.append(i + "msg=" + configData.getConfigurableMessages().get(i) + "\n");
        }

        Files.write(CONFIG_PATH, sb.toString().getBytes());
    }

    public static List<String> getDefaultMessages() {
        List<String> messages = new ArrayList<>();
        //General
        messages.add("This player does not exist or isn't online.");        //0
        messages.add("%player% is currently in 'do not disturb' mode.");    //1

        //SelfTP
        messages.add("You are really trying to teleport to yourself?");     //2

        //DoNotDisturb
        messages.add("Toggled off 'do not disturb' mode.");                 //3
        messages.add("AND YOU SHALL NOT BE DISTURBED NO MORE");             //4

        //Tpa
        messages.add("You are really trying to request a teleport to yourself?");       //5
        messages.add("You can't request a tp while in 'do not disturb' mode.");         //6
        messages.add("You already sent %player% a tpa request within the last %cooldown% seconds. You wouldn't want others to keep spamming you, would ya? Let's not make them regret accepting your request, alright?");  //7
        messages.add("%player% wants you to teleport to them."); //8
        messages.add("[Click here]"); //9
        messages.add("to teleport."); //10
        messages.add("Teleport request sent to %player%."); //11

        return messages;
    }
    
}
