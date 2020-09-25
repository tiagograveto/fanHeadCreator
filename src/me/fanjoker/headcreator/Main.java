package me.fanjoker.headcreator;

import me.fanjoker.headcreator.commands.HCCommand;
import me.fanjoker.headcreator.config.ConfigManager;
import me.fanjoker.headcreator.managers.*;
import me.fanjoker.headcreator.menus.Inventories;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import me.fanjoker.headcreator.listener.BreakEvent;
import me.fanjoker.headcreator.listener.InteractEvent;
import me.fanjoker.headcreator.listener.PlaceEvent;

public class Main extends JavaPlugin {

    private static Main instance;
    private static HeadCreatorMessages messages;
    public static ConfigManager config;

    private HCManager manager;
    private HCSettings settings;
    private HCConstructor constructor;
    private HCConnection connection;
    private Inventories inventories;

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
    public Inventories getInventories() {
        return inventories;
    }

    public void onEnable() {
        registerManagers();

        config.loadConfig("config");
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

        new BreakEvent(this);
        new PlaceEvent(this);
        new InteractEvent(this);
    }

//    ALL MANAGERS

    public static Main getInstance() { return instance; }
    public static HeadCreatorMessages getMessages() { return messages; }

//    REGISTER MANAGERS

    private void registerManagers() {
        instance = this;
        messages = new HeadCreatorMessages();
        constructor = new HCConstructor(this);
        settings = new HCSettings(this);
        manager = new HCManager(this);
        connection = new HCConnection();
        config = new ConfigManager();
        inventories = new Inventories(this);
    }

    public void log(String str) {
        getServer().getConsoleSender().sendMessage(str);
    }

    private void reloadHolograms() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            if (settings.useHolograms()) {
                log("§c[HeadCreator] §fRecebido hook de HolographicDisplays");

                if (Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
                    log("§c[HeadCreator] §fPlugin HolographicDisplays encontrado");
                    log("§c[HeadCreator] §fRecarregando hologramas...");
                    long start = System.nanoTime();
                    settings.reloadHolograms();
                    long elapsedTime = System.nanoTime() - start;
                    log("§c[HeadCreator] §fHologramas carregados com sucesso. (" + elapsedTime / 1000000 + "ms)");
                    return;

                }
                log("§c[HeadCreator] §fNão foi encontrado nenhum HolographicDisplays");
                log("§c[HeadCreator] §fDesativando função...");
                config.getConfig("config").getYaml().set("Config.UseHolograms", false);
                config.getConfig("config").save();
            }
        }, 50L);
    }

    public String getVersion() {
        return getDescription().getVersion();
    }
}
