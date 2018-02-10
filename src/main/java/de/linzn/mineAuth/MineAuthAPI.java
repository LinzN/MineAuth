package de.linzn.mineAuth;

import de.linzn.mineAuth.database.AuthQuery;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class MineAuthAPI {

    public static void tryToAuth(UUID playerUUID, String auth_key) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);
        if (AuthQuery.isAuth(playerUUID)) {
            player.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + "Du bist bereits verbunden!");
            return;
        }

        int userID = AuthQuery.getWSAccount(auth_key);
        if (userID == -1) {
            player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Der Authkey ist ungültig!");
            return;
        }

        if (!AuthQuery.saveAuth(playerUUID, userID, auth_key)) {
            player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "Es ist ein Fehler aufgetreten Type 1");
            return;
        }
        if (!AuthQuery.setUsername(userID, player.getName())) {
            player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "Es ist ein Fehler aufgetreten Type 2");
            return;
        }

        player.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Du hast dich erfolgreich mit deinem Forenaccount verbunden!");
    }
}
