package org.serverct.parrot.machinecommand.config;

import org.serverct.parrot.machinecommand.MachineCommand;
import org.serverct.parrot.parrotx.config.PConfig;

public class ConfigManager extends PConfig {
    public ConfigManager() {
        super(MachineCommand.getInstance(), "config", "主配置文件");
    }
}
