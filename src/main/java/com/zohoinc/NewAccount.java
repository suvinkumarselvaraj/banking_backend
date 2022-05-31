package com.zohoinc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.stream.Collectors;

import org.json.JSONObject;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NewAccount extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Headers", "*");
        String jsonBody = new BufferedReader(new InputStreamReader(req.getInputStream())).lines().collect(
                Collectors.joining("\n"));
        JSONObject jObj = new JSONObject(jsonBody);
        System.out.println("hello");
        System.out.println(jObj);
        jObj.put("status", "success");

        System.out.println("helloloooo world");
        PrintWriter out = res.getWriter();
        // out.write(jObj.toString());
        Connection con = DbConnection.returnConnection();
        String userName = jObj.getString("username");
        Long phoneNo = Long.parseLong(jObj.getString("phone"));
        String password = jObj.getString("password");
        boolean existng = false;
        long accountNumber;
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            // ResultSet rst = stmt.executeQuery("SELECT * FROM accounts");
            ResultSet rst = stmt
                    .executeQuery("SELECT customer_id FROM accounts WHERE phone_no = " +
                            phoneNo);
            if (rst.next()) {
                existng = true;
                jObj.put("isExistingUser", "existing");
            } else {

                ResultSet rst1 = stmt.executeQuery("SELECT COUNT(*) FROM accounts");
                System.out.println(rst1.getRow());
                rst1.next();
                int count = rst1.getInt(1);
                System.out.println(count);
                System.out.println("im here");
                if (count == 0) {
                    accountNumber = 11011;
                } else {
                    String queryString = "SELECT account_number from accounts ORDER BY created_at DESC LIMIT 1";
                    rst = stmt.executeQuery(queryString);
                    rst.next();
                    accountNumber = rst.getLong(1) + 11011;
                }
                jObj.put("isExistingUser", "non-existing");
                PreparedStatement pst = con.prepareStatement(
                        "INSERT INTO accounts(account_number,name,balance,phone_no,user_password) VALUES(?,?,?,?,?)");
                pst.setLong(1, accountNumber);
                pst.setString(2, userName);
                pst.setLong(3, 10000);
                pst.setLong(4, phoneNo);
                pst.setString(5, password);
                pst.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.write(jObj.toString());
    }
}