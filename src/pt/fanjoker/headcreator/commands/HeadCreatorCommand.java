package pt.fanjoker.headcreator.commands;

import com.mysql.jdbc.Buffer;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pt.fanjoker.headcreator.HeadCreator;
import pt.fanjoker.headcreator.menus.MenuGui;

public class HeadCreatorCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {
        if(s instanceof Player ) {
            Player p = (Player)s;
            if(p.hasPermission("headcreator.admin")) {
                if (args.length == 0) {
                    p.sendMessage(new String[] { "",
                            "  §e§lHeadCreator: §7Criação de cabeças",
                            "",
                            "  §e/hcreator info",
                            "  §e/hcreator give <player> <tipo>",
                            "  §e/hcreator menu",
                            "  §e/hcreator reload", ""});
                } else if(args.length == 1 && args[0].equalsIgnoreCase("info")) {
                    p.sendMessage(new String[]{"",
                            "§e Informações de §nfanHeadCreator",
                            "",
                            "§e Versão: §f1.0",
                            "§e Desenvolvedor: §fzFan",
                            "§e Contato:§f fan#7454", ""});

                } else if(args.length == 1 && args[0].equalsIgnoreCase("menu")) {

                    HeadCreator.getSettings().openInv(p);

                } else if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                    p.sendMessage("§aArquivo §f'config.yml' §arecarregado com êxito.");
                    HeadCreator.config.getConfig("config").reload();
                    if(HeadCreator.config.getConfig("config").getYaml().getBoolean("Config.UseHolograms")
                            && Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
                        HeadCreator.getSettings().reloadHolograms();
                        p.sendMessage("§aHologramas recarregados com êxito.");
                    }

                } else if(args.length <= 3 && args[0].equalsIgnoreCase("give")) {
                    if(args.length == 1) { p.sendMessage("§cUse: /hcreator give <player> <tipo>"); return true; }
                    if(args.length == 2) {
                        Player target = Bukkit.getPlayer(args[1]);
                        if(target == null) {
                            p.sendMessage("§cO Jogador §f'" + args[0] + "' §cse encontra offline");
                            return true;
                        }
                        p.sendMessage("§cUse: /hcreator give " + args[1] + " <tipo>");
                    } else {
                        Player target = Bukkit.getPlayer(args[1]);
                        String type = args[2];
                        if (!HeadCreator.getSettings().existsType(type)) {
                            p.sendMessage("§cO tipo §f'" + args[2] + "' §cnão foi encontrado na configuração");
                            return true;
                        }
                        HeadCreator.getSettings().sendHead(target, type);
                    }
                }
            } else {
                p.sendMessage("§cVocê não tem permissão para executar esse comando.");
            }
        } else {
            s.sendMessage("§cApenas jogadores podem usar esse comando.");
        }
        return false;
    }
}
