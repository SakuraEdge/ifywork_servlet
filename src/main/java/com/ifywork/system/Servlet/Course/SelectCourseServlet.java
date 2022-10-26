package com.ifywork.system.Servlet.Course;

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
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "SelectCourseServlet", value = "/SelectCourseServlet")
public class SelectCourseServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //数据流获取信息
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        char[] buf = new char[1024];
        int len;
        while ((len = reader.read(buf)) != -1){
            sb.append(buf,0,len);
        }
        String str = sb.toString();
        JSONObject jsonObject = JSONObject.parseObject(str);
        String teacherName = jsonObject.getString("teacherName");

        Map<String,String> courses;
        try {
            courses = DBUtil.selectCourseByTeacherName(teacherName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.getWriter().println(JSON.toJSONString(courses));
    }
}
