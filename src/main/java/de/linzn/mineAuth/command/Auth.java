package de.linzn.mineAuth.command;

import de.linzn.mineAuth.MineAuthAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class Auth extends Command {
    public Auth() {
        super("auth");
    }

    public void execute(CommandSender sender, String[] args) {
        this.auth(sender, args);
    }

    private void auth(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            UUID playerUUID = player.getUniqueId();
            if (args.length < 1) {
                player.sendMessage("§6-==============[§2§lMineGaming Auth§6]==============-");
                player.sendMessage("" + ChatColor.GREEN + "Mit MineGaming Auth kannst du deinen Forenaccount mit deinem Minecraftaccount verbinden!");
                player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Benutze: " + ChatColor.YELLOW + "/auth <Dein_KEY>");
                player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Beispiel: " + ChatColor.YELLOW + "/auth 1-4n75zSEgC8");
                return;
            }
            String key = args[0];
            MineAuthAPI.tryToAuth(playerUUID, key);
        }
    }
}
