package de.linzn.mineAuth.listener;

import de.linzn.mineAuth.MineAuthAPI;
import de.linzn.mineAuth.MineAuthPlugin;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onLogin(final LoginEvent e) {
        MineAuthPlugin.inst().getProxy().getScheduler().runAsync(MineAuthPlugin.inst(), () -> MineAuthAPI.updatePlayerName(e.getConnection().getName(), e.getConnection().getUniqueId()));
    }

}
