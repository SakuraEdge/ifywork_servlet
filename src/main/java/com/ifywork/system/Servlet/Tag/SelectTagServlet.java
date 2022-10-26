package com.ifywork.system.Servlet.Tag;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ifywork.system.Dao.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@WebServlet(name = "SelectTagServlet", value = "/SelectTagServlet")
public class SelectTagServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<String> tags;
        try {
            tags = DBUtil.selectTag();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.getWriter().println(JSON.toJSONString(tags));
    }
}
