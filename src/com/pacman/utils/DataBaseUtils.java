package com.pacman.utils;

import com.pacman.controller.GameController;
import com.pacman.entity.Pacman;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Vector;

public class DataBaseUtils {

    private static String DB_URL = "jdbc:mysql://localhost:3306/formaccount";
    private static String USER_NAME = "root";
    private static String PASSWORD = "";

    public static void savePlayerResult(String name, Date playDate, int Score, int level, boolean winState) {
        Connection conn = null;
        PreparedStatement pre = null;

        Pacman pac = null;
        try {
            pac = new Pacman();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        try {
            // Ket noi database
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);

            // query - insert
            String sql = "insert into userresult(username, date, score, level, status) values(?, ?, ?, ?, ?)";
            pre = conn.prepareStatement(sql);
            pre.setString(1, name);
            pre.setString(2, playDate.toString());
            pre.setInt(3, Score);
            pre.setInt(4, level);
            pre.setString(5, winState ? "Win" : "Lose");
            pre.execute();
            // close connection
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                pre.close();
                conn.close();

            } catch (SQLException e2) {
                // TODO: handle exception
            }
        }
    }

    public static String[][] getPlayerResult(int page) {
        //TODO chuoi: "Khang","2020","99999","8","WIN"
        // NEU CO HON 30 BAN GHI THI CHI LOAD 30
        return null;
    }


    private static int getHighestScore() throws SQLException {
        Connection conn = null;
        Statement statement = null;
        try {
            // Ket noi den database
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            System.out.println("Connecting to database...");
            // khoi tao doi
            statement = conn.createStatement();
            // Tạo câu truy vấn

            String sql = "select distinct score from userresult where userresult.score = (select max(score) from userresult) ";

            // Thuc thi
            ResultSet rs = statement.executeQuery(sql);


            while (rs.next()) {
                Integer score = rs.getInt("score");
                return score;
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        return 0;
    }
}
