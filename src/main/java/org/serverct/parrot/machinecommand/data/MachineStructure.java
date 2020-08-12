package org.serverct.parrot.machinecommand.data;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.serverct.parrot.machinecommand.MachineCommand;
import org.serverct.parrot.parrotstructure.data.Structure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MachineStructure extends Structure {

    private final Machine machine;

    public MachineStructure(NamespacedKey key, List<StructurePart> parts, Machine machine) {
        super(key, parts, machine.item);
        this.machine = machine;
    }

    @Override
    public void onInteract(PlayerInteractEvent event) {
        Player user = event.getPlayer();

        Map<String, String> placeholder = new HashMap<String, String>() {{
            Location userLoc = user.getLocation();

            put("{player}", user.getName());
            put("{player_world}", user.getWorld().getName());
            put("{player_x}", String.valueOf(userLoc.getBlockX()));
            put("{player_y}", String.valueOf(userLoc.getBlockY()));
            put("{player_z}", String.valueOf(userLoc.getBlockZ()));

            Block block = event.getClickedBlock();
            if (block != null) {
                Location blockLoc = block.getLocation();
                put("{player_world}", block.getWorld().getName());
                put("{player_x}", String.valueOf(blockLoc.getBlockX()));
                put("{player_y}", String.valueOf(blockLoc.getBlockY()));
                put("{player_z}", String.valueOf(blockLoc.getBlockZ()));
            }
        }};

        machine.commandList.forEach(command -> {
            String cmd = command;
            for (Map.Entry<String, String> entry : placeholder.entrySet()) {
                cmd = cmd.replace(entry.getKey(), entry.getValue());
            }
            if (MachineCommand.papi) {
                cmd = PlaceholderAPI.setPlaceholders(user, cmd);
            }
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        });
    }

    @Override
    public void onCreate(BlockPlaceEvent event) {

    }

    @Override
    public void onDestroy(BlockBreakEvent event) {

    }
}
