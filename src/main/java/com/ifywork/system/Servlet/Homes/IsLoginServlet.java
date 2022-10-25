package com.ifywork.system.Servlet.Homes;

import com.ifywork.system.Dao.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "IsLoginServlet", value = "/IsLoginServlet")
public class IsLoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = "";
        try {
            name = DBUtil.isLogin();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        PrintWriter out;
        out=response.getWriter();
        out.write(name);
    }
}
