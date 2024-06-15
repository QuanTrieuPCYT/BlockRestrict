package com.sylvcraft.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.sylvcraft.BlockRestrict;
import org.jetbrains.annotations.NotNull;

public class br implements CommandExecutor {
    BlockRestrict plugin;

    public br(BlockRestrict instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        try {
            String targetBlock;
            Player p = (sender instanceof Player) ? (Player) sender : null;
            if (!sender.hasPermission("blockrestrict.admin")) {
                plugin.msg("access-denied", sender);
                return true;
            }

            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    plugin.msg("specify-block", sender);
                    return true;
                }
                if (!p.getInventory().getItemInMainHand().getType().isBlock()) {
                    plugin.msg("not-a-block", sender);
                    return true;
                }

                targetBlock = p.getInventory().getItemInMainHand().getType().name();
            } else {
                if (args[0].equalsIgnoreCase("list")) {
                    listMaterials(sender);
                    return true;
                }

                Material m = Material.matchMaterial(args[0]);
                if (m == null) {
                    plugin.msg("invalid-item", sender);
                    return true;
                }
                if (!m.isBlock()) {
                    plugin.msg("not-a-block", sender);
                    return true;
                }

                targetBlock = m.name();
            }

            List<String> blocks = plugin.getConfig().getStringList("blocks");
            if (blocks.contains(targetBlock)) {
                blocks.remove(targetBlock);
                plugin.msg("removed", sender);
            } else {
                blocks.add(targetBlock);
                plugin.msg("added", sender);
            }

            plugin.getConfig().set("blocks", blocks);
            plugin.saveConfig();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    void listMaterials(CommandSender sender) {
        Map<String, String> data = new HashMap<>();
        plugin.msg("list-header", sender);
        for (Material m : Material.values()) {
            if (!m.isBlock()) continue;
            data.put("%value%", m.name());
            plugin.msg("list-data", sender, data);
        }
    }
}