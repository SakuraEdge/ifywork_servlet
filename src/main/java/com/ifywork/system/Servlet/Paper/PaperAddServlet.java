package com.ifywork.system.Servlet.Paper;

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

@WebServlet(name = "PaperAddServlet", value = "/PaperAddServlet")
public class PaperAddServlet extends HttpServlet {
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
        String name = jsonObject.getString("name");
        String a = jsonObject.getString("a");
        String b = jsonObject.getString("b");
        String c = jsonObject.getString("c");
        String d = jsonObject.getString("d");
        String reala = jsonObject.getString("reala");
        String knowledge = jsonObject.getString("knowledge");

        String msg="";
        try {
            msg = DBUtil.InsertPaper(name,a,b,c,d,reala,knowledge);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.getWriter().println(msg);

    }
}
