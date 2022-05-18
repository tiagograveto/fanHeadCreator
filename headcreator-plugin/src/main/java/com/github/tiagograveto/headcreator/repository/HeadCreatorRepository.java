package com.github.tiagograveto.headcreator.repository;

import com.github.tiagograveto.headcreator.Main;
import com.github.tiagograveto.headcreator.repository.connections.DatabaseConnection;
import com.github.tiagograveto.headcreator.repository.connections.MySQLDatabaseConnection;
import com.github.tiagograveto.headcreator.repository.connections.SQLiteDatabaseConnection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.sql.Connection;

public class HeadCreatorRepository {

    private Main main;
    private YamlConfiguration config;
    private DatabaseConnection databaseConnection;

    public HeadCreatorRepository(Main main) {
        this.main = main;
        this.config = main.getCfg();
        this.openDatabase();
    }

    public Connection getConnection() {
        return databaseConnection.getConnection();
    }

    public void closeConnection() {
        databaseConnection.closeConnection();
    }

    private void openDatabase() {
        boolean usesMysql = config.getBoolean("Mysql.enable");
        DatabaseConnection databaseConnection;

        if (usesMysql)
            databaseConnection = new MySQLDatabaseConnection(main);
        else
            databaseConnection = new SQLiteDatabaseConnection(main);

        databaseConnection.openConnection();
        this.databaseConnection = databaseConnection;
    }
}
