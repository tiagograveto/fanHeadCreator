package com.github.tiagograveto.headcreator.controllers;

import com.github.tiagograveto.headcreator.Main;
import com.github.tiagograveto.headcreator.entities.HCBlock;
import com.github.tiagograveto.headcreator.entities.HCConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HeadCreatorDatabaseController {

    private Main main;
    private Connection connection;

    public HeadCreatorDatabaseController(Main main, Connection connection) {
        this.main = main;
        this.connection = connection;
        this.loadHeadDatabase();
    }

    public void createHead(Location location, HCConfig hcConfig) {
        final String createQuery = "INSERT INTO `HeadCreator` (`location`, `type`, `toggle`) VALUES(?,?,?)";

        try (PreparedStatement stm = connection.prepareStatement(createQuery)) {
            stm.setString(1, serialize(location));
            stm.setString(2, hcConfig.getType());
            stm.setBoolean(3, true);
            stm.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        int id = getSQLId(location);
        HCBlock block = new HCBlock(location, hcConfig,true);

        main.getControllers().getCacheController().addHeadBlockInCache(id, block);
    }

    public void loadHeadDatabase() {
        final String loadQuery = "SELECT * FROM `HeadCreator`";

        try(PreparedStatement stm = connection.prepareStatement(loadQuery)) {
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {

                Location loc = deserialize(rs.getString("location"));
                HCConfig hcConfig = main.getControllers().getCacheController().getHeadConfigType(rs.getString("type"));

                if (hcConfig == null) {
                    main.error("");
                    main.error("§cOcorreu um erro ao recarregar a cabeça na localização: " + serialize(loc));
                    main.error("§cParece que não foi encontrado nenhum tipo dela, na configuração.");
                    main.error("§cDeletando a mesma...");
                    main.error("");

                    deleteHead(loc);
                }
                else if (!loc.getBlock().getType().equals(Material.SKULL)) {
                    main.error("");
                    main.error("§cOcorreu um erro ao recarregar a cabeça na localização: " + serialize(loc));
                    main.error("§cParece que a cabeça não foi encontrada como bloco no mundo overworld.");
                    main.error("§cDeletando a mesma...");
                    main.error("");

                    deleteHead(loc);
                }
                else {
                    int id = rs.getInt("id");
                    HCBlock hcBlock = new HCBlock(loc, hcConfig, rs.getBoolean("toggle"));

                    main.getControllers().getCacheController().addHeadBlockInCache(id, hcBlock);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteHead(Location loc) {
        final String deleteQuery = "DELETE FROM `HeadCreator` WHERE `id` = ?";

        loc.getBlock().setType(Material.AIR);
        int id = getSQLId(loc);

        try (PreparedStatement stm = connection.prepareStatement(deleteQuery)) {
            stm.setInt(1, id);
            stm.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        main.getControllers().getCacheController().getCacheBlocks().remove(id);
        main.getServices().getHologramService().reloadHeadHolograms();

    }

    public int getSQLId(Location loc) {
        final String idQuery = "SELECT `id` FROM `HeadCreator` WHERE `location` = ?";

        try (PreparedStatement stm = connection.prepareStatement(idQuery)) {
            stm.setString(1, serialize(loc));
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next() ? rs.getInt("id") : 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private String serialize (Location location) {
        return location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ();
    }

    private Location deserialize (String serialized) {
        String[] split = serialized.split(";");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]),
                Double.parseDouble(split[3]));
    }


    private void saveHead(int id) {
        final String saveQuery = "UPDATE `HeadCreator` SET `toggle` = ? WHERE id = ?";

        HCBlock hcBlock = main.getControllers().getCacheController().getCacheBlocks().get(id);
        try (PreparedStatement stm = connection.prepareStatement(saveQuery)) {

            stm.setBoolean(1, hcBlock.isToggle());
            stm.setInt(2, id);
            stm.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveHeadDatabase() {
        main.getControllers().getCacheController().getCacheBlocks().keySet().forEach(this::saveHead);
    }
}
