package org.serverct.parrot.machinecommand;

import org.bukkit.Bukkit;
import org.serverct.parrot.machinecommand.config.ConfigManager;
import org.serverct.parrot.machinecommand.config.MachineManager;
import org.serverct.parrot.parrotx.PPlugin;

public final class MachineCommand extends PPlugin {

    public static boolean papi = false;

    @Override
    protected void preload() {
        pConfig = new ConfigManager();
    }

    @Override
    protected void load() {
        new MachineManager().init();

        papi = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }
}
