package com.pacman.utils;


import java.sql.*;

public class DataBaseUtils {

    private static String DB_URL = "jdbc:mysql://localhost:3306/test";
    private static String USER_NAME = "root";
    private static String PASSWORD = "";

    public static void savePlayerResult(Date playDate, int Score, int level, boolean winState) {
        Connection conn = null;
        PreparedStatement pre = null;
        try {
            // Ket noi database
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            // query - insert
            String sql = "insert into playerscore(date, score, level, status) values(?, ?, ?, ?)";
            pre = conn.prepareStatement(sql);
            pre.setString(1, playDate.toString());
            pre.setInt(2, Score);
            pre.setInt(3, level);
            pre.setString(4, winState ? "Win" : "Lose");
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


    private static showData(int page) {

    }


    public static String[][] getPlayerResult(int page) {
        //TODO chuoi: "Khang","2020","99999","8","WIN"
        // NEU CO HON 30 BAN GHI THI CHI LOAD 30
        return null;
    }


    public static int getHighestScore() {
        Connection conn = null;
        Statement statement = null;
        try {
            // Ket noi den database
            conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
            // khoi tao doi
            statement = conn.createStatement();
            // Tạo câu truy vấn

            String sql = "select distinct score from playerscore where playerscore.score = (select max(score) from playerscore) ";

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
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                }
            }
        }
        return 0;
    }
}
