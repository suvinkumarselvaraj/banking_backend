package com.zohoinc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;
import org.json.JSONObject;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Transaction extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Headers", "*");
        String jsonBody = new BufferedReader(new InputStreamReader(req.getInputStream())).lines().collect(
                Collectors.joining("\n"));
        JSONObject jObj = new JSONObject(jsonBody);
        System.out.println(jObj);
        // query to find the old balance from the transaction db
        long accountNumber = Long.parseLong(jObj.getString("accountNumber"));
        int customerId = Integer.parseInt(jObj.getString("customerId"));
        long balance = Long.parseLong(jObj.getString("balance"));
        long transactionAmount = Long.parseLong(jObj.getString("amount"));
        String type = jObj.getString("transactionType");
        long newBalance = 0;

        // handle transaction types
        System.out.println(type);
        if (type.equals("deposit")) {
            System.out.println("deposit");
            balance = balance + transactionAmount;
        }
        if (type.equals("withdrawl"))
            balance = balance - transactionAmount;

        Connection con = DbConnection.returnConnection();

        try {
            PreparedStatement pst = con.prepareStatement(
                    "INSERT INTO transactions(customer_id,transaction_type, transaction_amount,balance) VALUES(?,?,?,?)");
            pst.setInt(1, customerId);
            pst.setString(2, type);
            pst.setLong(3, transactionAmount);
            pst.setLong(4, balance);
            pst.executeUpdate();

            pst = con.prepareStatement(
                    "UPDATE accounts SET balance = " + balance + " WHERE customer_id = " + customerId);
            pst.executeUpdate();

            jObj.put("status", "success");
            jObj.put("balance", balance + "");
            PrintWriter out = res.getWriter();
            out.write(jObj.toString());

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
