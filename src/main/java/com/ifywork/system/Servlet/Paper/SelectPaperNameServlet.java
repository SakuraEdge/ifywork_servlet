package com.ifywork.system.Servlet.Paper;

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

@WebServlet(name = "SelectPaperNameServlet", value = "/SelectPaperNameServlet")
public class SelectPaperNameServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        char[] buf = new char[1024];
        int len;
        while ((len = reader.read(buf)) != -1){
            sb.append(buf,0,len);
        }
        String str = sb.toString();
        JSONObject jsonObject = JSONObject.parseObject(str);

        String knowledge = jsonObject.getString("knowledge");
        ArrayList<String> tags;
        try {
            tags = DBUtil.selectPaperName(knowledge);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.getWriter().println(JSON.toJSONString(tags));
    }
}
