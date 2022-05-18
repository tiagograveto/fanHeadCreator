package com.github.tiagograveto.headcreator;

import com.github.tiagograveto.headcreator.bukkit.commands.HCreator_Command;
import com.github.tiagograveto.headcreator.bukkit.listener.BreakEvent;
import com.github.tiagograveto.headcreator.bukkit.listener.InteractEvent;
import com.github.tiagograveto.headcreator.bukkit.listener.PlaceEvent;
import com.github.tiagograveto.headcreator.config.Config;
import com.github.tiagograveto.headcreator.config.ConfigManager;
import com.github.tiagograveto.headcreator.config.Messages;
import com.github.tiagograveto.headcreator.controllers.HeadCreatorControllers;
import com.github.tiagograveto.headcreator.entities.ColorText;
import com.github.tiagograveto.headcreator.repository.HeadCreatorRepository;
import com.github.tiagograveto.headcreator.services.HeadCreatorServices;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static ConfigManager config;

    private HeadCreatorControllers controllers;
    private HeadCreatorServices services;
    private HeadCreatorRepository repository;

    public HeadCreatorControllers getControllers() {
        return controllers;
    }
    public HeadCreatorServices getServices() {
        return services;
    }

    public void onEnable() {
        registerManagers();

        config.loadConfig(this, "messages");
        config.loadConfig(this, "config");

        repository = new HeadCreatorRepository(this);
        controllers = new HeadCreatorControllers(this, repository.getConnection());
        services = new HeadCreatorServices(this);

        registerAll();
    }

    @Override
    public void onDisable() {
        controllers.getDatabaseController().saveHeadDatabase();
        repository.closeConnection();
    }

//    REGISTER LISTENERS/COMMANDS

    private void registerAll() {
        new HCreator_Command(this);

        new Config();
        new Messages();

        new BreakEvent(this);
        new PlaceEvent(this);
        new InteractEvent(this);
    }

//    REGISTER MANAGERS

    private void registerManagers() {
        config = new ConfigManager();
    }

    public YamlConfiguration getCfg() {
        return config.getConfig("config").getYaml();
    }
    public YamlConfiguration getMsg() { return config.getConfig("messages").getYaml(); }

    public void log(String str) {
        getServer().getConsoleSender().sendMessage(new ColorText("&c[HeadCreator] " + str).toString());
    }
    public void error(String error) {
        getServer().getConsoleSender().sendMessage(new ColorText("&c&l[ERROR] " + error).toString());
    }

    public String getVersion() {
        return getDescription().getVersion();
    }
}
