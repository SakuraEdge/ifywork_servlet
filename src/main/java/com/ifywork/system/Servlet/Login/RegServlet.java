package com.ifywork.system.Servlet.Login;

import com.alibaba.fastjson.JSONObject;
import com.ifywork.system.Dao.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.Random;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "RegServlet", value = "/RegServlet")
public class RegServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置传值的编码
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


        String name = jsonObject.getString("userName");
        String pwd = jsonObject.getString("password");
        String tel = jsonObject.getString("tel");

        Random random = new Random();
        String num = "";
        for (int i = 0; i < 10; i++) {
            num += String.valueOf(random.nextInt(10));
        }

        try {
            DBUtil.RegUser(num,name,pwd,tel);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        PrintWriter out;
        out=response.getWriter();
        out.write(num);
    }
}
