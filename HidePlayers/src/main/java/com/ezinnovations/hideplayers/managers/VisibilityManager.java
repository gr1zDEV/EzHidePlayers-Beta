package com.ezinnovations.hideplayers.managers;

import com.ezinnovations.hideplayers.HidePlayers;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class VisibilityManager {

    private static final String BYPASS_PERMISSION = "ezhideplayer.bypass";

    private final HidePlayers plugin;
    private final Set<UUID> hiddenViewers = new HashSet<>();

    public VisibilityManager(final HidePlayers plugin) {
        this.plugin = plugin;
    }

    public boolean toggle(final Player viewer) {
        if (hiddenViewers.contains(viewer.getUniqueId())) {
            disableAndShowAll(viewer);
            return false;
        }

        hiddenViewers.add(viewer.getUniqueId());
        hideEligiblePlayersFor(viewer);
        return true;
    }

    public boolean isToggled(final Player viewer) {
        return hiddenViewers.contains(viewer.getUniqueId());
    }

    public void clearToggleState(final Player player) {
        hiddenViewers.remove(player.getUniqueId());
    }

    public void disableAndShowAll(final Player viewer) {
        hiddenViewers.remove(viewer.getUniqueId());
        for (final Player target : plugin.getServer().getOnlinePlayers()) {
            viewer.showPlayer(plugin, target);
        }
    }

    public boolean canToggleInCurrentWorld(final Player player) {
        final String currentWorld = player.getWorld().getName();

        final List<String> disabledWorlds = plugin.getConfig().getStringList("disabled-worlds");
        if (disabledWorlds.contains(currentWorld)) {
            return false;
        }

        final List<String> allowedWorlds = plugin.getConfig().getStringList("allowed-worlds");
        return allowedWorlds.isEmpty() || allowedWorlds.contains(currentWorld);
    }

    public void reapplyForJoin(final Player joiningPlayer) {
        for (final Player viewer : plugin.getServer().getOnlinePlayers()) {
            if (viewer.getUniqueId().equals(joiningPlayer.getUniqueId())) {
                continue;
            }

            if (isToggled(viewer) && !joiningPlayer.hasPermission(BYPASS_PERMISSION)) {
                viewer.hidePlayer(plugin, joiningPlayer);
            } else {
                viewer.showPlayer(plugin, joiningPlayer);
            }
        }

        reapplyForPlayer(joiningPlayer);
    }

    public void reapplyForPlayer(final Player viewer) {
        if (!isToggled(viewer)) {
            for (final Player target : plugin.getServer().getOnlinePlayers()) {
                viewer.showPlayer(plugin, target);
            }
            return;
        }

        hideEligiblePlayersFor(viewer);
    }

    private void hideEligiblePlayersFor(final Player viewer) {
        for (final Player target : plugin.getServer().getOnlinePlayers()) {
            if (viewer.getUniqueId().equals(target.getUniqueId()) || target.hasPermission(BYPASS_PERMISSION)) {
                viewer.showPlayer(plugin, target);
                continue;
            }

            viewer.hidePlayer(plugin, target);
        }
    }
}
