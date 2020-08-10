package org.serverct.parrot.machinecommand;

import org.serverct.parrot.machinecommand.config.ConfigManager;
import org.serverct.parrot.machinecommand.config.MachineManager;
import org.serverct.parrot.parrotx.PPlugin;

public final class MachineCommand extends PPlugin {

    @Override
    protected void preload() {
        pConfig = new ConfigManager();
    }

    @Override
    protected void load() {
        new MachineManager().init();
    }
}
