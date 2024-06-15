package com.sylvcraft;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import com.sylvcraft.events.BlockPlace;

import com.sylvcraft.commands.br;


public class BlockRestrict extends JavaPlugin {
    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new BlockPlace(this), this);
        Objects.requireNonNull(getCommand("br")).setExecutor(new br(this));
        saveDefaultConfig();
    }

    public void msg(String msgCode, CommandSender sender) {
        String tmp = getConfig().getString("messages." + msgCode, msgCode) + ' ';
        if (tmp.trim().isEmpty()) return;
        for (String m : tmp.split("%br%")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', m));
        }
    }

    public void msg(String msgCode, CommandSender sender, Map<String, String> data) {
        String tmp = getConfig().getString("messages." + msgCode, msgCode) + ' ';
        if (tmp.trim().isEmpty()) return;
        for (Map.Entry<String, String> mapData : data.entrySet()) {
            tmp = tmp.replace(mapData.getKey(), mapData.getValue());
        }
        msg(tmp, sender);
    }
}