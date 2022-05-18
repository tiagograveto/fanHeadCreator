package com.github.tiagograveto.headcreator.repository.connections;

import com.github.tiagograveto.headcreator.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDatabaseConnection extends DatabaseConnection {

    public MySQLDatabaseConnection(Main main) {
        super(main);
    }

    @Override
    public void openConnection() {
        FileConfiguration config = Main.config.getConfig("config").getYaml();
        boolean usemysql = config.getBoolean("Mysql.enable");
        if (usemysql) {
            String hostname = config.getString("Mysql.hostname");
            String database_name = config.getString("Mysql.database");
            String username = config.getString("Mysql.username");
            String password = config.getString("Mysql.password");
            int port = config.getInt("Mysql.port");

            String URL = "jdbc:mysql://" + hostname + ":" + port + "/" + database_name;

            try {
                connection = DriverManager.getConnection(URL, username, password);
                createTable();
                log("&fConexão com o &fMySQL &afoi aberta!");
            } catch (SQLException e) {
                e.printStackTrace();
                log("&aConexão com o §fMySQL &afalhou, alterando modo para &fSQLITE!");
            }
            return;
        }
    }
}
