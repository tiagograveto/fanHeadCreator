package com.github.tiagograveto.headcreator.repository.connections;

import com.github.tiagograveto.headcreator.Main;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DatabaseConnection {

    protected Connection connection;
    protected Main main;

    public DatabaseConnection(Main main) {
        this.connection = null;
        this.main = main;
    }

    public Connection getConnection() {
        return connection;
    }

    public abstract void openConnection();

    protected void createTable() {
        final String query = "CREATE TABLE IF NOT EXISTS `HeadCreator` (`Id` INTEGER PRIMARY KEY, `location` TEXT, `toggle` TEXT, `type` TEXT)";
        try (PreparedStatement stm = connection.prepareStatement(query)) {

            stm.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            log("&6Não foi possível criar a tabela &fHeadCreator &6com a conexão MySQL.");

        }
    }

    public void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
                connection = null;
                log("&fConexão com o banco de dados foi fechada!");
            } catch (SQLException e) {
                e.printStackTrace();
                log("&fNão foi possível fechar a conexão com o banco de dados.");
            }
        }
    }

    protected void log(String str) {
        str = ChatColor.translateAlternateColorCodes('&', "&c[HeadCreator] " + str);
        System.out.println(str);
    }
}
