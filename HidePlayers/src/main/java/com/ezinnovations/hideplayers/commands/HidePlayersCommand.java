package com.ezinnovations.hideplayers.commands;

import com.ezinnovations.hideplayers.HidePlayers;
import com.ezinnovations.hideplayers.managers.VisibilityManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class HidePlayersCommand implements CommandExecutor {

    private final HidePlayers plugin;
    private final VisibilityManager visibilityManager;

    public HidePlayersCommand(final HidePlayers plugin, final VisibilityManager visibilityManager) {
        this.plugin = plugin;
        this.visibilityManager = visibilityManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!(sender instanceof final Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }

        if (!player.hasPermission("ezhideplayer")) {
            player.sendMessage(translate("messages.no-permission"));
            return true;
        }

        if (!visibilityManager.canToggleInCurrentWorld(player)) {
            player.sendMessage(translate("messages.world-disabled"));
            return true;
        }

        final boolean nowEnabled = visibilityManager.toggle(player);
        player.sendMessage(nowEnabled ? translate("messages.enabled") : translate("messages.disabled"));
        return true;
    }

    private String translate(final String path) {
        final String raw = plugin.getConfig().getString(path, "");
        return ChatColor.translateAlternateColorCodes('&', raw);
    }
}
