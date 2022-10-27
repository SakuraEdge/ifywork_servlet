package com.ifywork.system.Servlet.Userset;

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

@WebServlet(name = "ResetPwdServlet", value = "/ResetPwdServlet")
public class ResetPwdServlet extends HttpServlet {
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

        String id = jsonObject.getString("id");
        String old_pwd = jsonObject.getString("old_pwd");
        String new_pwd = jsonObject.getString("new_pwd");

        String msg = "";

        try {
            msg = DBUtil.resetPassWord(id,old_pwd,new_pwd);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.getWriter().println(msg);

    }
}
