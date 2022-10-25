package com.ifywork.system.Dao;

import com.ifywork.system.pojo.ClassStudent;
import com.ifywork.system.pojo.MyClass;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
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
    public static boolean LoginUser(String uname, String upwd) {
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

    public static String isLogin() throws SQLException {
        Statement st = c.createStatement();
        String sql = "select * from isdb where type = 'isLogin'";
        ResultSet rs = st.executeQuery(sql);
        if (rs.next())
        {
            String name = rs.getString("value");
            rs.close();
            return name;
        }
        rs.close();
        return "null";
    }

    public static String selectName(String id) throws SQLException {
        Statement st = c.createStatement();
        String sql = String.format("select * from user where Number = '%s'",id);
        ResultSet rs = st.executeQuery(sql);
        if (rs.next())
        {
            String name = rs.getString("name");
            rs.close();
            return name;
        }
        rs.close();
        return "null";
    }

    public static void setLogin(String name) throws SQLException {
        PreparedStatement ps;
        //3.获取用于向数据库发送sql语句的statement
        Statement st = c.createStatement();
        String sql = String.format("update isdb set value='%s' where type = 'isLogin'",name);
        ps = c.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
        st.close();
    }

    public static  Map<String, String> selectClassStudent(String classname) throws SQLException {
        //3.获取用于向数据库发送sql语句的statement
        Statement st = c.createStatement();
        String sql = String.format("select * from student_%s",classname);
        ResultSet rs=st.executeQuery(sql);


        Map<String, String> map = new HashMap<String, String>();
        while (rs.next())
        {
            String name = rs.getString("name");
            String id = rs.getString("Number");
            map.put(name,id);
        }
        System.out.println(map);
        return map;
    }

    public static List<MyClass> selectClassByTeacherID(String teacherID) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = String.format("select * from class where teacherID='%s'",teacherID);
        ResultSet rs=stmt.executeQuery(sql);
        List<MyClass> classes = new ArrayList<>();

        while (rs.next())
        {
            classes.add(new MyClass(rs.getString("classname"),teacherID,rs.getInt("maxNum"),
                    rs.getString("address")));
        }

        return classes;
    }

    public static void insertClass(MyClass myClass) throws SQLException {
        PreparedStatement ps;
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = formatter.format(date);

        String sql = String.format("insert into class(classname,teacherID,maxNum,address,createTime) " +
                        "values('%s','%s',%d,'%s','%s')",
                myClass.getName(),myClass.getTeacherID(),myClass.getMaxNum(),myClass.getAddress(),time);
        ps = c.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();

        createClassTableOfStudent(myClass.getName());

        System.out.println("添加成功！");
    }

    public static void createClassTableOfStudent(String name) throws SQLException {
        PreparedStatement ps = null;
        ps = c.prepareStatement("create table if not exists Student_"+ name + "(" +
                "id int not null primary key auto_increment," +
                "Number varchar(50)," +
                "name varchar(50)," +
                "createPerson varchar(50) default 'system'," +
                "createTime datetime ON UPDATE CURRENT_TIMESTAMP," +
                "updatePerson varchar(50)," +
                "updateTime datetime ON UPDATE CURRENT_TIMESTAMP" +
                ")");
        ps.executeUpdate();
        ps.close();
    }



    public static String insertStudentToClass(String className,String studentName,String studentID) throws SQLException {
        Statement stmt = c.createStatement();
        String sql;
        PreparedStatement ps;
        ResultSet rs;

        sql="select * from 'Student_" + className  + "' where number='" + studentID + "'";
        rs=stmt.executeQuery(sql);
        if(rs.next()) {
            return "该学生已存在班级中！";
        }


        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = formatter.format(date);

        sql = "INSERT INTO student_"  + className +" (Number,name,createPerson,createTime) VALUES(?,?,?,?)";
        ps = c.prepareStatement(sql);
        ps.setString(1, studentID);
        ps.setString(2, studentName);
        ps.setString(3, "system");
        ps.setString(4, time);
        ps.executeUpdate();//执行添加数据
        ps.close();
        return "添加成功！";
    }


    public static List<String> selectClass(String teacherID) throws SQLException {
        Statement stmt = c.createStatement();
        List<String> classname = new ArrayList<String>();
        String sql;
        sql = String.format("select * from class where teacherID='%s'",teacherID);
        ResultSet rs=stmt.executeQuery(sql);
        while(rs.next()){
            classname.add(rs.getString("classname"));
        }
        stmt.close();
        rs.close();
        return classname;
    }


    public static void deleteStudent(String classname,String id) throws SQLException {
        PreparedStatement ps;
        //3.获取用于向数据库发送sql语句的statement
        Statement st = c.createStatement();
        String sql = String.format("delete from student_%s where Number='%s'",classname,id);
        ps = c.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
        st.close();
    }

    public static void deleteClass(String classname) throws SQLException {
        PreparedStatement ps;
        //3.获取用于向数据库发送sql语句的statement
        Statement st = c.createStatement();
        String sql = String.format("drop table student_%s",classname);
        ps = c.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
        sql = String.format("delete from class where classname='%s'",classname);
        ps = c.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
        st.close();
    }
}
