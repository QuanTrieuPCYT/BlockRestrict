package com.sylvcraft.events;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.sylvcraft.BlockRestrict;
import org.bukkit.event.block.BlockPlaceEvent;


public class BlockPlace implements Listener {
    BlockRestrict plugin;

    public BlockPlace(BlockRestrict instance) {
        plugin = instance;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getPlayer().hasPermission("blockrestrict.bypass")) return;
        if (e.getPlayer().hasPermission("blockrestrict.bypass." + e.getBlockPlaced().getType().name().toLowerCase()))
            return;
        List<String> blocks = plugin.getConfig().getStringList("blocks");
        if (!blocks.contains(e.getBlockPlaced().getType().name())) return;

        plugin.msg("not-allowed", e.getPlayer());
        e.setCancelled(true);
    }
}