package pt.fanjoker.headcreator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import pt.fanjoker.headcreator.commands.HeadCreatorCommand;
import pt.fanjoker.headcreator.config.ConfigManager;
import pt.fanjoker.headcreator.constructor.HCManager;
import pt.fanjoker.headcreator.listener.HeadCreatorBreak;
import pt.fanjoker.headcreator.listener.HeadCreatorInteract;
import pt.fanjoker.headcreator.listener.HeadCreatorPlace;
import pt.fanjoker.headcreator.managers.HeadCreatorConnection;
import pt.fanjoker.headcreator.managers.HeadCreatorManager;
import pt.fanjoker.headcreator.managers.HeadCreatorMessages;
import pt.fanjoker.headcreator.managers.HeadCreatorSettings;
import pt.fanjoker.headcreator.menus.MenuGui;
import pt.fanjoker.headcreator.utils.ScrollerInventoryListener;

import java.util.HashMap;
import java.util.Map;

public class HeadCreator extends JavaPlugin {

    private static HeadCreator instance;
    private static HeadCreatorManager manager;
    private static HeadCreatorMessages messages;
    private static HeadCreatorSettings settings;
    private static HeadCreatorConnection connection;
    private static Map<Integer, HCManager> list = new HashMap<>();
    public static ConfigManager config;

    public void onEnable() {
        registerManagers();
        config.loadConfig("config");
        getConnection().openConnectionMySQL();
        loadHeads();
        registerAll();
        reloadHolograms();
    }

    @Override
    public void onDisable() {
        saveHeads();
        getConnection().close();
    }

    private void saveHeads() {
        for (int id : getHeadList().keySet()) {
            boolean bool = getHeadList().get(id).isToggle();
            getManager().updateBoolean(id, bool);
        }
    }
    private void loadHeads() {
        for (int id : getManager().getSQLAllIds()) {
            Location loc = getManager().getSQLLocation(id);
            boolean toggle = getManager().getSQLToggle(id);
            String type = getManager().getSQLType(id);
            String owner = getManager().getSQLTarget(id);
            HCManager hcm = new HCManager(loc, toggle, type, owner);
            getHeadList().put(id, hcm);
        }
        Bukkit.getConsoleSender().sendMessage("§c[HeadCreator] §fForam recarregadas " + getHeadList().size() + " cabeças.");
    }

//    REGISTER LISTENERS/COMMANDS

    private void registerAll() {
        getCommand("hcreator").setExecutor(new HeadCreatorCommand());
        Bukkit.getPluginManager().registerEvents(new HeadCreatorPlace(), this);
        Bukkit.getPluginManager().registerEvents(new HeadCreatorBreak(), this);
        Bukkit.getPluginManager().registerEvents(new HeadCreatorInteract(), this);
        Bukkit.getPluginManager().registerEvents(new MenuGui(), this);
        Bukkit.getPluginManager().registerEvents(new ScrollerInventoryListener(), this);
    }

//    ALL MANAGERS

    public static Map<Integer, HCManager> getHeadList() { return list;}
    public static HeadCreator getInstance() { return instance; }
    public static HeadCreatorManager getManager() { return manager; }
    public static HeadCreatorMessages getMessages() { return messages; }
    public static HeadCreatorConnection getConnection() { return connection; }
    public static HeadCreatorSettings getSettings() { return settings; }

//    REGISTER MANAGERS

    private void registerManagers() {
        instance = this;
        manager = new HeadCreatorManager();
        messages = new HeadCreatorMessages();
        settings = new HeadCreatorSettings();
        connection = new HeadCreatorConnection();
        config = new ConfigManager();
    }

    private void reloadHolograms() {
        new BukkitRunnable() {
            public void run() {
                if (config.getConfig("config").getYaml().getBoolean("Config.UseHolograms")) {
                    getServer().getConsoleSender().sendMessage("§c[HeadCreator] §fRecebido hook de HolographicDisplays");
                    if (Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
                        getServer().getConsoleSender().sendMessage("§c[HeadCreator] §fPlugin HolographicDisplays encontrado");
                        getServer().getConsoleSender().sendMessage("§c[HeadCreator] §fRecarregando hologramas...");
                        long start = System.nanoTime();
                        HeadCreator.getSettings().reloadHolograms();
                        long elapsedTime = System.nanoTime() - start;
                        getServer().getConsoleSender().sendMessage("§c[HeadCreator] §fHologramas carregados com sucesso. (" + elapsedTime / 1000000 + "ms)");

                    } else {
                        getServer().getConsoleSender().sendMessage("§c[HeadCreator] §fNão foi encontrado nenhum HolographicDisplays");
                        getServer().getConsoleSender().sendMessage("§c[HeadCreator] §fDesativando boolean");
                        config.getConfig("config").getYaml().set("Config.UseHolograms", false);
                        config.getConfig("config").save();
                    }
                }
            }
        }.runTaskLater(HeadCreator.getInstance(), 50L);
    }


}
