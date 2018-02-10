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

public class DatabaseServerSetup {
    public static boolean create() {
        return mysql();
    }

    private static boolean mysql() {
        String db = Config.getString("mysql.database");
        int port = Config.getInt("mysql.port");
        String host = Config.getString("mysql.host");
        String url = "jdbc:mysql://" + host + ":" + port + "/" + db;
        String username = Config.getString("mysql.username");
        String password = Config.getString("mysql.password");
        ConnectionFactory factory = new ConnectionFactory(url, username, password);
        ConnectionManager manager = ConnectionManager.DEFAULT;
        ConnectionHandler handler = manager.getHandler("mineAuth", factory);

        try {
            Connection connection = handler.getConnection();
            String data = "CREATE TABLE IF NOT EXISTS connected_ws_user (uuid CHAR(36) NOT NULL, wsID INT(11) NOT NULL, minegaming_auth_key VARCHAR(255) NOT NULL, PRIMARY KEY (uuid));";
            Statement action = connection.createStatement();
            action.executeUpdate(data);
            action.close();
            handler.release(connection);
            MineAuthPlugin.inst().getLogger().info("Database Bungee SERVER loaded!");
            return true;

        } catch (Exception e) {
            MineAuthPlugin.inst().getLogger().warning("============mineAuth-Error=============");
            MineAuthPlugin.inst().getLogger().warning("Unable to connect to Bungee SERVER database.");
            MineAuthPlugin.inst().getLogger().warning("Pls check you mysql connection in config.yml.");
            MineAuthPlugin.inst().getLogger().warning("============mineAuth-Error=============");
            return false;
        }

    }

}
