package com.github.tiagograveto.headcreator.controllers;

import com.github.tiagograveto.headcreator.Main;

import java.sql.Connection;

public class HeadCreatorControllers {

    private HeadCreatorCacheController cacheController;
    private HeadCreatorDatabaseController databaseController;

    public HeadCreatorControllers(Main main, Connection connection) {
        cacheController = new HeadCreatorCacheController(main.getCfg());
        databaseController = new HeadCreatorDatabaseController(main, connection);
    }

    public HeadCreatorCacheController getCacheController() {
        return cacheController;
    }

    public HeadCreatorDatabaseController getDatabaseController() {
        return databaseController;
    }
}
