package com.ezinnovations.hideplayers.listeners;

import com.ezinnovations.hideplayers.HidePlayers;
import com.ezinnovations.hideplayers.managers.VisibilityManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class VisibilityListener implements Listener {

    private final HidePlayers plugin;
    private final VisibilityManager visibilityManager;

    public VisibilityListener(final HidePlayers plugin, final VisibilityManager visibilityManager) {
        this.plugin = plugin;
        this.visibilityManager = visibilityManager;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        visibilityManager.reapplyForJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        visibilityManager.clearToggleState(event.getPlayer());
    }

    @EventHandler
    public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();

        if (!visibilityManager.canToggleInCurrentWorld(player)
                && visibilityManager.isToggled(player)
                && plugin.getConfig().getBoolean("force-show-in-disabled-worlds", true)) {
            visibilityManager.disableAndShowAll(player);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("messages.world-disabled", "")));
            return;
        }

        visibilityManager.reapplyForPlayer(player);
    }
}
