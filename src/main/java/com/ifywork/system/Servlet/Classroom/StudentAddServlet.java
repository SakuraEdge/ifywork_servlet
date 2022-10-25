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
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Objects;

@WebServlet(name = "StudentAddServlet", value = "/StudentAddServlet")
public class StudentAddServlet extends HttpServlet {
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

        String className = jsonObject.getString("classname");
        String studentID = jsonObject.getString("studentid");
        String studentName = "";
        try {
            studentID = DBUtil.selectName(studentID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String msg = "";
        if (Objects.equals(studentID, "null")){
            PrintWriter out;
            out=response.getWriter();
            out.write("错误,该学生不存在！");
        }

        else {
            try {
                msg = DBUtil.insertStudentToClass(className,studentName,studentID);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        response.getWriter().println(msg);
    }
}
