package org.serverct.parrot.machinecommand.config;

import org.serverct.parrot.machinecommand.MachineCommand;
import org.serverct.parrot.machinecommand.data.Machine;
import org.serverct.parrot.parrotx.config.PFolder;
import org.serverct.parrot.parrotx.utils.BasicUtil;
import org.serverct.parrot.parrotx.utils.I18n;

import java.io.File;

public class MachineManager extends PFolder {
    public MachineManager() {
        super(MachineCommand.getInstance(), "Machines", "结构数据文件夹", "MACHINE");
    }

    @Override
    public void load(File file) {
        try {
            Machine machine = new Machine(file, buildId(BasicUtil.getNoExFileName(file.getName())));
            machine.structure.register();
            put(machine);
        } catch (Throwable e) {
            plugin.lang.logError(I18n.LOAD, getTypeName() + "/" + file.getName(), e, null);
        }
    }
}
