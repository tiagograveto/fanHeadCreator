package pt.fanjoker.headcreator.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import pt.fanjoker.headcreator.HeadCreator;
import pt.fanjoker.headcreator.constructor.HCManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HeadCreatorManager {

    private static Connection con = null;

    private Connection getConnection() {
        return HeadCreator.getConnection().getConnection();
    }

    public int getId(Location loc) {
        for (int i : HeadCreator.getHeadList().keySet()) {
            if (HeadCreator.getHeadList().get(i).getLoc().equals(loc)) {
                return i;
            }
        }
        return 0;
    }
    public boolean existsHead(Location loc) {
        for (int num : HeadCreator.getHeadList().keySet()) {
            if (HeadCreator.getHeadList().get(num).getLoc().equals(loc)) {
                return true;
            }
        }
        return false;
    }
    public void setToggle(int id, boolean toggle) {
        HeadCreator.getHeadList().get(id).setToggle(toggle);
    }
    public String getTarget (int id) { return HeadCreator.getHeadList().get(id).getOwner(); }
    public Location getLocation (int id) { return HeadCreator.getHeadList().get(id).getLoc(); }
    public String getType(int id) { return HeadCreator.getHeadList().get(id).getType(); }
    public boolean getToggle(int id) { return HeadCreator.getHeadList().get(id).isToggle(); }


    public List<Location> getAllLocs() {
        List<Location> list = new ArrayList<>();
        for (Integer num : HeadCreator.getHeadList().keySet())
            list.add(HeadCreator.getHeadList().get(num).getLoc());

        return list;
    }
    public List<Integer> getAllIds() {
        List<Integer> list = new ArrayList<>();
        for (Integer num : HeadCreator.getHeadList().keySet())
            list.add(num);

        return list;
    }

//
//    SQL ACTIONS
//

    public void create(String player, Location location, String type, String date) {
        PreparedStatement stm = null;
        try {
            stm = getConnection().prepareStatement("INSERT INTO `HeadCreator` (`target`, `location`, `type`, `toggle`, `date`) VALUES(?,?,?,?,?)");
            stm.setString(1, player);
            stm.setString(2, serialize(location));
            stm.setString(3, type);
            stm.setBoolean(4, true);
            stm.setString(5, date);
            stm.executeUpdate();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§cNão foi possivel criar a cabeça '" + type + "' para o jogador '" + player + "'");
        }
        HCManager hcm = new HCManager(location, true, type, player);
        HeadCreator.getHeadList().put(getSQLId(location), hcm);
    }
    public void deleteHead(Location location) {
        PreparedStatement stm = null;
        try {
            stm = getConnection().prepareStatement("DELETE FROM `HeadCreator` WHERE `location` = ?");
            stm.setString(1, serialize(location.clone()));
            stm.execute();
            stm.close();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("§cNão foi possivel deletar uma cabeça");
        }
        Bukkit.getScheduler().runTaskLater(HeadCreator.getInstance(), () -> {
            for (int num : HeadCreator.getHeadList().keySet()) {
                if (HeadCreator.getHeadList().get(num).getLoc().equals(location)) {
                    HeadCreator.getHeadList().remove(num);
                }
            }
        }, 5L);
    }

    public void updateBoolean(int id, boolean bool) {
        PreparedStatement stm = null;
        try {
            stm = getConnection().prepareStatement("UPDATE `HeadCreator` SET `toggle` = ? WHERE `id` = ?");
            stm.setBoolean(1, bool);
            stm.setInt(2, id);
            stm.execute();
            stm.close();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("§cNão foi possivel dar update em uma cabeça");
        }
    }

    public List<Integer> getSQLAllIds() {
        List<Integer> ids = new ArrayList<>();
        try {
            PreparedStatement stm = null;
            stm = getConnection().prepareStatement("SELECT `id` FROM `HeadCreator`");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                ids.add(rs.getInt("id"));
            }
            rs.close();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }
    public int getSQLId(Location loc) {
        try {
            PreparedStatement stm = null;
            stm = getConnection().prepareStatement("SELECT `id` FROM `HeadCreator` WHERE `location` = ?");
            stm.setString(1, serialize(loc));
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
            rs.close();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public String getSQL(String path, int id) {
        try {
            PreparedStatement stm = null;
            stm = getConnection().prepareStatement("SELECT * FROM `HeadCreator` WHERE `id` = ?");
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getString(path);
            }
            rs.close();
            stm.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSQLTarget (int id) { return getSQL("target", id); }
    public Location getSQLLocation (int id) { return deserialize(getSQL("location", id)); }
    public String getSQLType(int id) { return getSQL("type", id); }
    public boolean getSQLToggle(int id) { return getSQL("toggle", id).equalsIgnoreCase("1"); }


    public String serialize (Location locations) {

        return locations.getWorld().getName() + ";" + locations.getX() + ";" + locations.getY() + ";" + locations.getZ();
    }

    public Location deserialize (String serialized) {
        String[] split = serialized.split(";");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]),
                Double.parseDouble(split[3]));
    }
}
