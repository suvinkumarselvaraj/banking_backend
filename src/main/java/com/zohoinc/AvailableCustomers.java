package com.zohoinc;

import com.zohoinc.DbConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AvailableCustomers extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Headers", "*");
        String query = "SELECT account_number from accounts";
        Statement stmt;
        Connection con = DbConnection.returnConnection();
        try {
            stmt = con.createStatement();
            ResultSet rst = stmt.executeQuery(query);
            JSONArray array = new JSONArray();
            while (rst.next()) {

                JSONObject json = new JSONObject();

                json.put("accountNumber", rst.getString(1));
                array.put(json);
            }
            PrintWriter out = res.getWriter();
            out.write(array.toString());

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
