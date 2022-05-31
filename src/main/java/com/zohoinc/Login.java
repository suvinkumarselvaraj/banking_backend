package com.zohoinc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.PrintWriter;
import java.math.BigInteger;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import java.util.stream.Collectors;

public class Login extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Headers", "*");
        String jsonBody = new BufferedReader(new InputStreamReader(req.getInputStream())).lines().collect(
                Collectors.joining("\n"));
        JSONObject jObj = new JSONObject(jsonBody);
        System.out.println(jObj);

        long accountNum = Long.parseLong(jObj.getString("accountNumber"));
        BigInteger accountNumber = new BigInteger(jObj.getString("accountNumber"));
        String password = jObj.getString("password");
        String accNo = jObj.getString("accountNumber");
        System.out.println(accountNumber);
        PrintWriter out = res.getWriter();
        Connection con = DbConnection.returnConnection();
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rst = stmt.executeQuery(
                    "SELECT * FROM accounts WHERE account_number =" + accountNum + " AND user_password = '"
                            + password + "'");
            System.out.println(rst.getRow());
            // "INSERT INTO accounts(account_number,name,balance,phone_no,user_password)
            // VALUES(?,?,?,?,?)");
            if (rst.next()) {
                jObj.put("status", "success");
                jObj.put("username", rst.getString("name"));
                jObj.put("balance", rst.getLong("balance"));
                jObj.put("phoneNo", rst.getLong("phone_no"));
                jObj.put("customerId", rst.getInt("customer_id"));
            } else {
                jObj.put("status", "failure");
            }
            out.write(jObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}