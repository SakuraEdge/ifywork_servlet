package com.ifywork.system;

import com.ifywork.system.User;
import java.util.Random;
import java.sql.*;

public class DBUtil {
    static Connection c;

    static {
        String url = "jdbc:mysql://localhost:3306/system?useUnicode=true&characterEncoding=utf8&useSSL=true";
        //连接的数据库时使用的用户名
        String username = "root";
        //连接的数据库时使用的密码
        String password = "123456";
        //1.加载驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//推荐使用这种方式来加载驱动
            c = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean LoginUser(String uname, String upwd)  {
        //要连接的数据库URL


        try {
            //2.获取与数据库的链接
            //3.获取用于向数据库发送sql语句的statement
            Statement st = c.createStatement();
            String sql = String.format("select * from user where name='%s' and password='%s'",uname,upwd);
            //4.向数据库发sql,并获取代表结果集的resultset
            ResultSet rs = st.executeQuery(sql);
            //5.取出结果集的数据
            if (rs.next()) {
                System.out.println(rs);
                return true;
            }
            rs.close();
            st.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void RegUser(String num,String name, String pwd, String tel) throws SQLException {
        //要连接的数据库URL
        String url = "jdbc:mysql://localhost:3306/system?useUnicode=true&characterEncoding=utf8&useSSL=true";
        //连接的数据库时使用的用户名
        String username = "root";
        //连接的数据库时使用的密码
        String password = "123456";
        try {
            PreparedStatement ps;
            //3.获取用于向数据库发送sql语句的statement
            Statement st = c.createStatement();
            String sql = String.format("insert into user(Number,name,password,tel) values('%s','%s','%s','%s')",num,name,pwd,tel);
            ps = c.prepareStatement(sql);
            ps.executeUpdate();
            ps.close();
            st.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
