package com.github.tiagograveto.headcreator.services;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.github.tiagograveto.headcreator.Main;
import com.github.tiagograveto.headcreator.config.Config;
import com.github.tiagograveto.headcreator.entities.ColorText;
import com.github.tiagograveto.headcreator.entities.HCBlock;
import com.github.tiagograveto.headcreator.entities.HCConfig;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class HologramDisplaysService {

    private Main main;

    public HologramDisplaysService(Main main) {
        this.main = main;
        this.hookHolograms();
    }

    private void hookHolograms() {
        Bukkit.getScheduler().runTaskLater(main, () -> {
            if (Config.USE_HOLOGRAMS) {
                main.log("&fRecebido hook de HolographicDisplays");

                if (Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null) {
                    main.log("&fPlugin HolographicDisplays encontrado");
                    main.log("&fRecarregando hologramas...");
                    long start = System.nanoTime();
                    if (this.reloadHeadHolograms()) {
                        long elapsedTime = System.nanoTime() - start;
                        main.log("&fHologramas carregados com sucesso. (" + elapsedTime / 1000000 + "ms)");
                    }
                    return;

                }
                main.log("&fNão foi encontrado nenhum HolographicDisplays");
                main.log("&fSe você não utilizar esse serviço, desative-o no arquivo de configuração");
            }
        }, 50L);
    }

    public boolean reloadHeadHolograms() {
        try {
            HologramsAPI.getHolograms(main).forEach(Hologram::delete);
            for (HCBlock hcBlock : main.getControllers().getCacheController().getCacheBlocks().values()) {
                if (hcBlock.getConfig().usesHologram())
                    createHologram(hcBlock);

            }
            return true;

        } catch (Exception e) {
            main.error("&cAconteceu um erro ao recarregar os hologramas, tente reiniciar o servidor.");
            return false;
        }
    }

    public void createHologram(HCBlock hcBlock) {

        HCConfig type = hcBlock.getConfig();
        Location loc = hcBlock.getLoc().clone();

        if(type.usesHologram()) {

            loc.add(0.5, type.getHeight(), 0.5);

            Hologram hologram = HologramsAPI.createHologram(main, loc);
            type.getHologram().stream().map(str -> new ColorText(str).toString()).forEach(hologram::appendTextLine);

        }
    }
}
