package com.zohoinc;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Example extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        System.out.println("Helo world");
    }
}