package me.fanjoker.headcreator;

import me.fanjoker.headcreator.commands.HCCommand;
import me.fanjoker.headcreator.config.Config;
import me.fanjoker.headcreator.config.ConfigManager;
import me.fanjoker.headcreator.managers.*;
import me.fanjoker.headcreator.menus.Menus;
import me.fanjoker.headcreator.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import me.fanjoker.headcreator.listener.BreakEvent;
import me.fanjoker.headcreator.listener.InteractEvent;
import me.fanjoker.headcreator.listener.PlaceEvent;

public class Main extends JavaPlugin {

    private static Main instance;
    public static ConfigManager config;

    private HCManager manager;
    private HCSettings settings;
    private HCConstructor constructor;
    private HCConnection connection;
    private Menus inventories;

    public HCConstructor getConstructor() {
        return constructor;
    }
    public HCSettings getSettings() { return settings; }
    public HCManager getManager() {
        return manager;
    }
    public HCConnection getConnection() {
        return connection;
    }
    public Menus getInventories() {
        return inventories;
    }

    public void onEnable() {
        registerManagers();

        config.loadConfig("config");
        config.loadConfig("messages");
        connection.openConnectionMySQL();

        settings.loadHeadConfigs();
        manager.loadHeadDatabase();

        registerAll();
        reloadHolograms();
    }

    @Override
    public void onDisable() {
        manager.saveHeadDatabase();
        connection.close();
    }

//    REGISTER LISTENERS/COMMANDS

    private void registerAll() {
        new HCCommand(this);

        new Config();
        new Messages();

        new BreakEvent(this);
        new PlaceEvent(this);
        new InteractEvent(this);
    }

//    ALL MANAGERS

    public static Main getInstance() { return instance; }

//    REGISTER MANAGERS

    private void registerManagers() {
        instance = this;
        constructor = new HCConstructor(this);
        settings = new HCSettings(this);
        manager = new HCManager(this);
        connection = new HCConnection(this);
        config = new ConfigManager();
        inventories = new Menus(this);
    }

    public YamlConfiguration getCfg() {
        return config.getConfig("config").getYaml();
    }
    public YamlConfiguration getMsg() { return config.getConfig("messages").getYaml(); }

    private String colortext(String str) {
        return settings.colorText(str);
    }

    public void log(String str) {
        getServer().getConsoleSender().sendMessage(colortext("&c[HeadCreator] " + str));
    }

    public void error(String error) {
        getServer().getConsoleSender().sendMessage(colortext("&c&l[ERROR] " + error));
    }

    private void reloadHolograms() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            if (Config.USE_HOLOGRAMS) {
                log("&fRecebido hook de HolographicDisplays");

                if (Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
                    log("&fPlugin HolographicDisplays encontrado");
                    log("&fRecarregando hologramas...");
                    long start = System.nanoTime();
                    if (settings.reloadHolograms()) {
                        long elapsedTime = System.nanoTime() - start;
                        log("&fHologramas carregados com sucesso. (" + elapsedTime / 1000000 + "ms)");
                    }
                    return;

                }
                log("&fNão foi encontrado nenhum HolographicDisplays");
                log("&fDesativando função...");
                config.getConfig("config").getYaml().set("Config.UseHolograms", false);
                config.getConfig("config").save();
            }
        }, 50L);
    }

    public String getVersion() {
        return getDescription().getVersion();
    }
}
