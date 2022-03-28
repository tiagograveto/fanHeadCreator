package me.fanjoker.headcreator.commands;

import me.fanjoker.headcreator.Main;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.xml.soap.Text;
import java.util.Arrays;

public class HCCommand implements CommandExecutor {

    private Main main;

    public HCCommand(Main main) {
        this.main = main;
        main.getCommand("hcreator").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

        if (!(s instanceof Player)) {
            s.sendMessage("§cApenas jogadores in-game, podem executar esse comando.");
            return true;
        }

        if (!s.hasPermission("hcreator.admin")) {
            s.sendMessage("§cVocê não tem permissão para executar esse comando.");
            return true;
        }

        Player p = (Player) s;

        if (args.length == 0) {
            p.sendMessage(new String[]{
                    "",
                    " §e§lHeadCreator: §7Criação de cabeças",
                    "",
                    "  §e/hcreator info",
                    "  §e/hcreator give <player> <tipo>",
                    "  §e/hcreator menu",
                    "  §e/hcreator reload",
                    ""});
            return true;
        }

        if (args[0].equalsIgnoreCase("menu"))
            main.getInventories().getMenuGUI().openInv(p);

        if (args[0].equalsIgnoreCase("info")) {
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


        if (args[0].equalsIgnoreCase("reload")) {
            Main.config.getConfig("config").reload();
            p.sendMessage("§aArquivo §f'config.yml' §arecarregado com êxito.");
            if (main.getCfg().getBoolean("Config.UseHolograms")) {
                main.getSettings().reloadHolograms();
                p.sendMessage("§aHologramas recarregados com êxito.");
            }

        }

        if (args.length <= 3 && args[0].equalsIgnoreCase("give")) {

            if (args.length <= 2) {
                p.sendMessage("§cUse: /hcreator give <player> <tipo>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);
            String type = args[2];

            if (target == null) {
                p.sendMessage("§cO jogador §f'" + args[1] + "' §cse encontra offline no momento.");
                return true;
            }

            if (!main.getSettings().existsType(type)) {
                p.sendMessage("§cO tipo §f'" + args[2] + "' §cnão foi encontrado na configuração.");
                return true;
            }
            main.getSettings().giveHead(target, main.getSettings().getType(type));

        }
        return false;
    }
}
