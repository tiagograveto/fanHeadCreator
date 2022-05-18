package com.github.tiagograveto.headcreator.bukkit.commands;

import com.github.tiagograveto.headcreator.Main;
import com.github.tiagograveto.headcreator.config.Config;
import com.github.tiagograveto.headcreator.config.Messages;
import com.github.tiagograveto.headcreator.entities.HCConfig;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HCreator_Command implements CommandExecutor {

    private Main main;

    public HCreator_Command(Main main) {
        this.main = main;
        main.getCommand("hcreator").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

        if (!(s instanceof Player)) {
            s.sendMessage(Messages.ONLY_PLAYERS);
            return true;
        }

        if (!s.hasPermission(Config.PERMISSION)) {
            s.sendMessage(Messages.NO_PERMISSION);
            return true;
        }

        Player p = (Player) s;

        if (args.length == 0) {
            p.sendMessage(Messages.COMMAND_INFO);
            return true;
        }

        if (args[0].equalsIgnoreCase("menu")) {
            main.getServices().getInventoryService().getHeadPanelInventory().openInventory(p, 0);

        }
        else if (args[0].equalsIgnoreCase("info")) {
            p.sendMessage(new String[]{"",
                    "§e Informações de §nfanHeadCreator",
                    "",
                    "§e  Versão: §f" + main.getVersion(),});

            TextComponent project = new TextComponent("§e  Projeto: ");
            TextComponent git = new TextComponent("§6§lCLIQUE AQUI");
            git.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eClique para abrir a change-log").create()));
            git.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/tgraveto/fanHeadCreator/releases/"));
            p.spigot().sendMessage(new TextComponent[] { project, git });

            p.sendMessage(new String[] { "§e  Contato:§f fan#7454", ""});

        }


        else if (args[0].equalsIgnoreCase("reload")) {

            for (String str : Main.config.getConfigs().keySet()) {
                p.sendMessage(Messages.RELOAD_FILE.replace("%file%", str + ".yml"));
                Main.config.getConfig(str).reload();
            }

            if (Config.USE_HOLOGRAMS) {
                main.getServices().getHologramService().reloadHeadHolograms();
                p.sendMessage(Messages.RELOAD_HOLOGRAMS);
            }

        }

        else if (args.length <= 3 && args[0].equalsIgnoreCase("give")) {

            if (args.length <= 2) {
                p.sendMessage("§cUse: /hcreator give <player> <tipo>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            String type = args[2];
            HCConfig hcConfig = main.getControllers().getCacheController().getHeadConfigType(type);

            if (target == null) {
                p.sendMessage(Messages.ONLY_PLAYERS.replace("%target%", args[1]));
                return true;
            }

            if (hcConfig == null) {
                p.sendMessage(Messages.TYPE_NOT_FOUND.replace("%type%", args[2]));
                return true;
            }

            hcConfig.giveHeadConfigItem(target);
        }
        else if (args[0].equalsIgnoreCase("list")) {
            main.getControllers().getCacheController().printHeadConfigTypes(p);
        }
        return false;
    }
}
