/*
 * Copyright (C) 2018. MineGaming - All Rights Reserved
 *  You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 *  You should have received a copy of the LGPLv3 license with
 *  this file. If not, please write to: niklas.linz@enigmar.de
 */

package de.linzn.mineAuth.database;

import de.linzn.mineAuth.Config;
import de.linzn.mineAuth.MineAuthPlugin;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseForumSetup {
    public static boolean create() {
        return mysql();

    }

    private static boolean mysql() {
        String db = Config.getString("mysqlWCF.database");
        int port = Config.getInt("mysqlWCF.port");
        String host = Config.getString("mysqlWCF.host");
        String url = "jdbc:mysql://" + host + ":" + port + "/" + db;
        String username = Config.getString("mysqlWCF.username");
        String password = Config.getString("mysqlWCF.password");
        ConnectionFactory factory = new ConnectionFactory(url, username, password);
        ConnectionManager manager = ConnectionManager.DEFAULT;
        ConnectionHandler handler = manager.getHandler("mineAuthWS", factory);

        try {
            Connection connection = handler.getConnection();
            Statement action = connection.createStatement();
            action.close();
            handler.release(connection);
            MineAuthPlugin.inst().getLogger().info("Database WS Forum loaded!");
            return true;

        } catch (Exception e) {
            MineAuthPlugin.inst().getLogger().warning("============MineAuth-Error=============");
            MineAuthPlugin.inst().getLogger().warning("Unable to connect to WS Forum.");
            MineAuthPlugin.inst().getLogger().warning("Pls check you mysql connection in config.yml.");
            MineAuthPlugin.inst().getLogger().warning("============MineAuth-Error=============");
            return false;
        }

    }

}
