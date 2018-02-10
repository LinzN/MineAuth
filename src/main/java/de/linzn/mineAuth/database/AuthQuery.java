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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthQuery {

    private static String KEY_FIELD = "userOption39";
    private static String USERNAME_FIELD = "userOption30";
    private static String IS_AUTH_FIELD = "userOption40";

    public static boolean isAuth(UUID playerUUID) {
        boolean isAuth = false;
        ConnectionManager manager = ConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("mineAuth");
            PreparedStatement sql = conn.prepareStatement(
                    "SELECT * FROM connected_ws_user WHERE uuid = '" + playerUUID + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                isAuth = true;
            }
            result.close();
            sql.close();
            manager.release("mineAuth", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAuth;
    }

    public static boolean isAlreadyAuth(int userID) {
        boolean isAuth = false;
        ConnectionManager manager = ConnectionManager.DEFAULT;
        try {
            Connection conn = manager.getConnection("mineAuth");
            PreparedStatement sql = conn.prepareStatement(
                    "SELECT * FROM connected_ws_user WHERE wsID = '" + userID + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                isAuth = true;
            }
            result.close();
            sql.close();
            manager.release("mineAuth", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAuth;
    }

    public static boolean saveAuth(UUID playerUUID, int wsID, String auth_key) {
        ConnectionManager manager = ConnectionManager.DEFAULT;
        try {

            Connection conn = manager.getConnection("mineAuth");
            PreparedStatement sql = conn.prepareStatement("INSERT INTO connected_ws_user (uuid, wsID, minegaming_auth_key) values (?, ?, ?)");
            /* Set data */
            sql.setString(1, playerUUID.toString());
            sql.setInt(2, wsID);
            sql.setString(3, auth_key);
            /* Execute query*/
            sql.executeUpdate();
            sql.close();
            manager.release("mineAuth", conn);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* WS Connection */

    public static int getWSAccount(String auth_key) {
        ConnectionManager manager = ConnectionManager.DEFAULT;
        int wsID = -1;
        try {
            Connection conn = manager.getConnection("mineAuthWS");
            PreparedStatement sql = conn.prepareStatement(
                    "SELECT userID FROM wcf1_user_option_value WHERE " + KEY_FIELD + " = '" + auth_key + "';");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                wsID = result.getInt("userID");
            }
            result.close();
            sql.close();
            manager.release("mineAuthWS", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wsID;
    }


    public static String getWSAccountName(int userID) {
        ConnectionManager manager = ConnectionManager.DEFAULT;
        String wsName = null;
        try {
            Connection conn = manager.getConnection("mineAuthWS");
            PreparedStatement sql = conn.prepareStatement(
                    "SELECT username FROM wcf1_user WHERE userID = " + userID + ";");
            ResultSet result = sql.executeQuery();
            if (result.next()) {
                wsName = result.getString("username");
            }
            result.close();
            sql.close();
            manager.release("mineAuthWS", conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wsName;
    }

    public static boolean updateWSAccount(int wsID, String userName) {
        ConnectionManager manager = ConnectionManager.DEFAULT;
        try {

            Connection conn = manager.getConnection("mineAuthWS");
            PreparedStatement sql = conn.prepareStatement("UPDATE wcf1_user_option_value SET " + USERNAME_FIELD + " = '" + userName + "', " + IS_AUTH_FIELD + " = '" + 1 + "' WHERE userID = " + wsID);
            /* Execute query*/
            sql.executeUpdate();
            sql.close();
            manager.release("mineAuthWS", conn);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
