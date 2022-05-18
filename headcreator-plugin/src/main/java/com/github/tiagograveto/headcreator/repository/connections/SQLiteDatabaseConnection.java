package com.github.tiagograveto.headcreator.repository.connections;

import com.github.tiagograveto.headcreator.Main;

import java.io.File;
import java.sql.DriverManager;

public class SQLiteDatabaseConnection extends DatabaseConnection {

    public SQLiteDatabaseConnection(Main main) {
        super(main);
    }

    @Override
    public void openConnection() {

        File file = new File(main.getDataFolder(), "database.db");

        String URL = "jdbc:sqlite:" + file;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(URL);
            createTable();
            log("&fConexão com o SQLite foi aberta!");
        } catch (Exception e) {
            e.printStackTrace();
            log("&aConexão com o SQLite falhou, desabilitando o plugin");
        }
    }
}
