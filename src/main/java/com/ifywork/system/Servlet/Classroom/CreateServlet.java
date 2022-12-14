package com.ifywork.system.Servlet.Classroom;

import com.alibaba.fastjson.JSONObject;
import com.ifywork.system.Dao.DBUtil;
import com.ifywork.system.pojo.MyClass;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "CreateServlet", value = "/CreateServlet")
public class CreateServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("utf-8");

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

        String teacherID =" ";
        try {
            teacherID = DBUtil.isLogin();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        int maxnum = jsonObject.getIntValue("num");
        String address = jsonObject.getString("address");
        String name = jsonObject.getString("name");

        MyClass myClass = new MyClass(name,teacherID,maxnum,address);



        try {
            DBUtil.insertClass(myClass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        PrintWriter out;
        out=response.getWriter();
        out.write("创建成功！");


    }
}
