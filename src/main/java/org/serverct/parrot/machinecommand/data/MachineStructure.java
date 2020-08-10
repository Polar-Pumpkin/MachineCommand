package org.serverct.parrot.machinecommand.data;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.serverct.parrot.parrotstructure.data.Structure;

import java.util.List;

public class MachineStructure extends Structure {

    private final Machine machine;

    public MachineStructure(NamespacedKey key, List<StructurePart> parts, Machine machine) {
        super(key, parts);
        this.machine = machine;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        machine.commandList.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

    @Override
    public void onCreate(BlockPlaceEvent event) {

    }

    @Override
    public void onDestroy(BlockBreakEvent event) {

    }
}
