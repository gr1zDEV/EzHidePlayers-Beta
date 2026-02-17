package com.ezinnovations.hideplayers;

import com.ezinnovations.hideplayers.commands.HidePlayersCommand;
import com.ezinnovations.hideplayers.listeners.VisibilityListener;
import com.ezinnovations.hideplayers.managers.VisibilityManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class HidePlayers extends JavaPlugin {

    private VisibilityManager visibilityManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.visibilityManager = new VisibilityManager(this);

        final HidePlayersCommand commandExecutor = new HidePlayersCommand(this, visibilityManager);
        if (getCommand("hideplayers") != null) {
            getCommand("hideplayers").setExecutor(commandExecutor);
        }

        getServer().getPluginManager().registerEvents(new VisibilityListener(this, visibilityManager), this);
    }

    public VisibilityManager getVisibilityManager() {
        return visibilityManager;
    }
}
