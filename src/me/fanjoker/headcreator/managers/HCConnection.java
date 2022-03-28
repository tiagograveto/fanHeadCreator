package me.fanjoker.headcreator.managers;

import me.fanjoker.headcreator.Main;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HCConnection {

    private static Connection con = null;
    private String prefix = "HeadCreator";

    private Main main;

    public HCConnection(Main main) {
        this.main = main;
    }

    public Connection getConnection()
    {
        return con;
    }

    private void createTables() {
        table1();
    }
    private void table1() {
        try (PreparedStatement stm = con.prepareStatement("CREATE TABLE IF NOT EXISTS `" + prefix + "`(" +
                "`Id` INTEGER PRIMARY KEY, `location` TEXT, `toggle` TEXT, `type` TEXT)")) {

            stm.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            main.log("&6Não foi possível criar a tabela &f" + prefix);

        }
    }

    public void openConnectionMySQL() {

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
                con = DriverManager.getConnection(URL, username, password);
                createTables();
                main.log("&fConexão com o &fMySQL &afoi aberta!");
            } catch (SQLException e) {
                e.printStackTrace();
                main.log("&aConexão com o §fMySQL &afalhou, alterando modo para &fSQLITE!");
            }
            return;

        }
        openConnectionSQLite();
    }
    private void openConnectionSQLite() {
        File file = new File(Main.getInstance().getDataFolder(), "database.db");

        String URL = "jdbc:sqlite:" + file;

        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection(URL);
            createTables();
            main.log("&fConexão com o SQLite foi aberta!");
        } catch (Exception e) {
            e.printStackTrace();
            main.log("&aConexão com o SQLite falhou, desabilitando o plugin");
        }
    }

    public void close() {
        if(con != null) {
            try {
                con.close();
                con = null;
                main.log("&fConexão com o banco de dados foi fechada!");
            } catch (SQLException e) {
                e.printStackTrace();
                main.log("&fNão foi possível fechar a conexão com o banco de dados.");
            }
        }
    }

}
