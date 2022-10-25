package com.ifywork.system.Servlet.Classroom;


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

@WebServlet(name = "CreateServlet", value = "/CreateServlet")
public class CreateStuServlet extends HttpServlet {
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

        String classname = jsonObject.getString("classname");
        String id = jsonObject.getString("id");
        String name = jsonObject.getString("name");

        String msg = "";
        try {
            msg = DBUtil.insertStudentToClass(classname,id,name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.getWriter().println(msg);
    }
}
