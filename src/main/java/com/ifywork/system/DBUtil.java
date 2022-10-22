package com.ifywork.system;

import com.ifywork.system.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBUtil {
    static {
        //1.加载驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//推荐使用这种方式来加载驱动
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static User DBGetUser(String uname, String upwd)  {
        //要连接的数据库URL
        String url = "jdbc:mysql://localhost:3306/system?useUnicode=true&characterEncoding=utf8&useSSL=true";
        //连接的数据库时使用的用户名
        String username = "root";
        //连接的数据库时使用的密码
        String password = "123456";

        try {
            //2.获取与数据库的链接
            Connection conn = DriverManager.getConnection(url, username, password);
            //3.获取用于向数据库发送sql语句的statement
            Statement st = conn.createStatement();
            String sql = "select id,name,password from userid";
            //4.向数据库发sql,并获取代表结果集的resultset
            ResultSet rs = st.executeQuery(sql);
            //5.取出结果集的数据
            while (rs.next()) {
                Integer tb_id = (Integer) rs.getObject("id");
                String tb_uname = (String) rs.getObject("name");
                String tb_upwd = (String) rs.getObject("password");
                if (null != tb_uname && tb_uname.equals(uname) && tb_upwd.equals(upwd)) {
                    User user = new User();
                    user.setId(tb_id);
                    user.setUname(tb_uname);
                    user.setUpwd(tb_upwd);
                    return user;
                }
            }
            rs.close();
            st.close();
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
