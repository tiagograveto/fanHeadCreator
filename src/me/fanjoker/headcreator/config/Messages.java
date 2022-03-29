package me.fanjoker.headcreator.config;

import me.fanjoker.headcreator.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Messages {

    public static YamlConfiguration config;
    public static String ONLY_PLAYERS;
    public static String NO_PERMISSION;
    public static String[] COMMAND_INFO;
    public static String RELOAD_FILE;
    public static String RELOAD_HOLOGRAMS;
    public static String PLAYER_OFFLINE;
    public static String TYPE_NOT_FOUND;
    public static String PLACED_HEAD;
    public static String BREAK_HEAD;

    public Messages() {
        config = Main.config.getConfig("messages").getYaml();
        ONLY_PLAYERS = getString("ONLY_PLAYERS");
        NO_PERMISSION = getString("NO_PERMISSION");
        COMMAND_INFO = getStringList("COMMAND_INFO");
        RELOAD_FILE = getString("RELOAD_FILE");
        RELOAD_HOLOGRAMS = getString("RELOAD_HOLOGRAMS");
        PLAYER_OFFLINE = getString("PLAYER_OFFLINE");
        TYPE_NOT_FOUND = getString("TYPE_NOT_FOUND");
        PLACED_HEAD = getString("PLACED_HEAD");
        BREAK_HEAD = getString("BREAK_HEAD");
    }

    public static String getString(String str) {
        return colorText(config.getString(str, "&cNão foi possível localizar a mensagem &f'" + str + "' &cno arquivo &fmessages.yml."));
    }

    public static String[] getStringList(String str) {
        return config.getStringList(str).stream().map(Messages::colorText).toArray(String[]::new);
    }

    private static String colorText(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
