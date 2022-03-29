package me.fanjoker.headcreator.config;

import me.fanjoker.headcreator.Main;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

    public static YamlConfiguration config;
    public static String PERMISSION;
    public static boolean OPEN_WITH_SHIFT;
    public static boolean DISABLE_HOLOGRAM;
    public static boolean USE_HOLOGRAMS;

    public Config() {
        config = Main.config.getConfig("config").getYaml();
        PERMISSION = config.getString("Config.PERMISSION");
        OPEN_WITH_SHIFT = config.getBoolean("Config.OPEN_WITH_SHIFT");
        DISABLE_HOLOGRAM = config.getBoolean("Config.DISABLE_HOLOGRAM");
        USE_HOLOGRAMS = config.getBoolean("Config.USE_HOLOGRAMS");
    }
}
