package de.linzn.mineAuth;

import de.linzn.mineAuth.command.Auth;
import de.linzn.mineAuth.database.DatabaseForumSetup;
import de.linzn.mineAuth.database.DatabaseServerSetup;
import de.linzn.mineAuth.listener.BungeeListener;
import net.md_5.bungee.api.plugin.Plugin;


public class MineAuthPlugin extends Plugin {
    private static MineAuthPlugin inst;

    public MineAuthPlugin() {
    }

    public static MineAuthPlugin inst() {
        return inst;
    }

    public void onDisable() {
    }

    public void onEnable() {
        inst = this;
        Config fileManager = new Config(this);
        fileManager.setDefaultConfig();
        if (DatabaseServerSetup.create() && DatabaseForumSetup.create()) {
            this.getProxy().getPluginManager().registerCommand(this, new Auth());
            this.getProxy().getPluginManager().registerListener(this, new BungeeListener());
        } else {
            this.getLogger().severe("Plugin disable because of database break!");
        }
    }


}
