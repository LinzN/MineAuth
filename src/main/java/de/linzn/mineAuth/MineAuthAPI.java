package de.linzn.mineAuth;

import de.linzn.mineAuth.database.AuthQuery;
import de.linzn.mineGuild.MineGuildPlugin;
import de.linzn.mineGuild.database.GuildDatabase;
import de.linzn.mineGuild.objects.GuildPlayer;
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

        if (AuthQuery.isAlreadyAuth(userID)) {
            player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Auf diesen Forenaccount wurde bereits ein ingame Account verbunden!");
            return;
        }

        String wsName = AuthQuery.getWSAccountName(userID);

        if (wsName == null) {
            player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Es ist ein Fehler aufgetreten Type 0");
            return;
        }

        if (!AuthQuery.saveAuth(playerUUID, userID, auth_key)) {
            player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "Es ist ein Fehler aufgetreten Type 1");
            return;
        }
        String guildName = null;
        GuildPlayer guildPlayer = GuildDatabase.getGuildPlayer(playerUUID);
        if (guildPlayer != null){
            guildName = guildPlayer.getGuild().guildName;
        }

        if (!AuthQuery.updateWSAccount(userID, player.getName(), guildName)) {
            player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "Es ist ein Fehler aufgetreten Type 2");
            return;
        }

        player.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Du hast dich erfolgreich mit deinem Forenaccount " + ChatColor.YELLOW + wsName + ChatColor.GREEN + ChatColor.BOLD + " verbunden!");
    }

    public static void updatePlayerName(final String playerName, final UUID uuid){
        int wsID = AuthQuery.getWbbID(uuid);
        if(wsID != -1){
            String guildName = null;
            GuildPlayer guildPlayer = GuildDatabase.getGuildPlayer(uuid);
            if (guildPlayer != null){
                guildName = guildPlayer.getGuild().guildName;
            }
            MineAuthPlugin.inst().getLogger().info("Updating WS forum name...");
            AuthQuery.updateWSAccount(wsID, playerName, guildName);
        }
    }
}
