package com.zohoinc;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

    public static Connection con;

    public static Connection returnConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/banking-app-test", "root", "");
            return con;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

}