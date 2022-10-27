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
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = formatter.format(date);
        try {
            PreparedStatement ps;
            //3.获取用于向数据库发送sql语句的statement
            Statement st = c.createStatement();
            String sql = String.format("insert into user(Number,name,password,tel,createTime) values('%s','%s','%s','%s','%s')",num,name,pwd,tel,time);
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

        sql="select * from Student_" + className  + " where number='" + studentID + "'";
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

    public static Map<String,String> selectInfo(String name) throws SQLException {
        Statement stmt = c.createStatement();
        Map<String, String> map = new HashMap<String, String>();
        String sql;
        sql = String.format("select * from user where name='%s'",name);
        ResultSet rs=stmt.executeQuery(sql);
        while(rs.next()){
            map.put("num",rs.getString("Number"));
            map.put("name",rs.getString("name"));
            map.put("tel",rs.getString("tel"));
            map.put("createTime",rs.getString("createTime"));
        }
        stmt.close();
        rs.close();
        return map;
    }

    public static Map<String,String> selectCourseByTeacherName(String teacherName) throws SQLException {
        Statement stmt = c.createStatement();
        Map<String,String> map = new HashMap<>();
        String sql;
        sql = String.format("select * from course where teacherName='%s'", teacherName);
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            map.put(rs.getString("name"),rs.getString("tag"));
        }
        stmt.close();
        rs.close();
        return map;
    }


    public static String InsertCourse(String courseName,String teacherName,String tag) throws SQLException {
        Statement stmt = c.createStatement();
        String sql;
        PreparedStatement ps;
        ResultSet rs;

        sql = String.format("select * from course where name = '%s'",courseName);
        rs=stmt.executeQuery(sql);

        if(rs.next()) {
            return "该课程已存在班级中！";
        }


        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = formatter.format(date);

        sql = "INSERT INTO course (name,teacherName,tag,createPerson,createTime) VALUES(?,?,?,?,?)";
        ps = c.prepareStatement(sql);
        ps.setString(1, courseName);
        ps.setString(2, teacherName);
        ps.setString(3, tag);
        ps.setString(4, "system");
        ps.setString(5, time);
        ps.executeUpdate();//执行添加数据
        ps.close();
        return "添加成功！";
    }

    public static void deleteCourse(String courseName) throws SQLException {
        PreparedStatement ps;
        //3.获取用于向数据库发送sql语句的statement
        Statement st = c.createStatement();
        String sql = String.format("delete from course where name='%s'",courseName);
        ps = c.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
        st.close();
    }

    public static void deleteTag(String tag) throws SQLException {
        PreparedStatement ps;
        //3.获取用于向数据库发送sql语句的statement
        Statement st = c.createStatement();
        String sql = String.format("delete from tag where name='%s'",tag);
        ps = c.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
        st.close();
    }

    public static String InsertTag(String tag) throws SQLException {
        Statement stmt = c.createStatement();
        String sql;
        PreparedStatement ps;
        ResultSet rs;

        sql = String.format("select * from tag where name = '%s'",tag);
        rs=stmt.executeQuery(sql);

        if(rs.next()) {
            return "该Tag已存在！";
        }

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = formatter.format(date);

        sql = "INSERT INTO tag (name,createPerson,createTime) VALUES(?,?,?)";
        ps = c.prepareStatement(sql);
        ps.setString(1, tag);
        ps.setString(2, "system");
        ps.setString(3, time);
        ps.executeUpdate();//执行添加数据
        ps.close();
        return "添加成功！";

    }

    public static ArrayList<String> selectTag() throws SQLException {
        Statement stmt = c.createStatement();
        ArrayList<String> array = new ArrayList<String>();
        String sql;
        sql = "select * from tag";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            array.add(rs.getString("name"));
        }
        stmt.close();
        rs.close();
        return array;
    }

    public static String InsertKnowledge(String knowledge) throws SQLException {
        Statement stmt = c.createStatement();
        String sql;
        PreparedStatement ps;
        ResultSet rs;

        sql = String.format("select * from knowledge where name = '%s'",knowledge);
        rs=stmt.executeQuery(sql);

        if(rs.next()) {
            return "该知识点已存在！";
        }

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = formatter.format(date);

        sql = "INSERT INTO knowledge (name,createPerson,createTime) VALUES(?,?,?)";
        ps = c.prepareStatement(sql);
        ps.setString(1, knowledge);
        ps.setString(2, "system");
        ps.setString(3, time);
        ps.executeUpdate();//执行添加数据
        ps.close();
        return "添加成功！";
    }

    public static String deleteKnowledge(String knowledge) throws SQLException {
        PreparedStatement ps;
        //3.获取用于向数据库发送sql语句的statement
        Statement st = c.createStatement();
        String sql = String.format("select * from paper where knowledge='%s'",knowledge);
        ResultSet rs;
        rs=st.executeQuery(sql);
        if (rs.next()){
            return "该知识点下存在试题，无法删除！";
        }
        rs.close();

        sql = String.format("delete from knowledge where name='%s'",knowledge);
        ps = c.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
        st.close();
        return "删除成功！";
    }
    public static ArrayList<String> selectKnowledge() throws SQLException {
        Statement stmt = c.createStatement();
        ArrayList<String> array = new ArrayList<String>();
        String sql;
        sql = "select * from knowledge";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            array.add(rs.getString("name"));
        }
        stmt.close();
        rs.close();
        return array;
    }

    public static String InsertPaper(String name,String a,String b,String cc,String d,String reala,String knowledge) throws SQLException {
        Statement stmt = c.createStatement();
        String sql;
        PreparedStatement ps;
        ResultSet rs;

        sql = String.format("select * from paper where name = '%s'",name);
        rs=stmt.executeQuery(sql);

        if(rs.next()) {
            return "该试题已存在！";
        }

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = formatter.format(date);

        sql = "INSERT INTO paper (name,a,b,c,d,reala,knowledge,createPerson,createTime) VALUES(?,?,?,?,?,?,?,?,?)";
        ps = c.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, a);
        ps.setString(3, b);
        ps.setString(4, cc);
        ps.setString(5, d);
        ps.setString(6, reala);
        ps.setString(7, knowledge);
        ps.setString(8, "system");
        ps.setString(9, time);
        ps.executeUpdate();//执行添加数据
        ps.close();
        return "添加成功！";
    }

    public static  List<String> selectPaper(String papername) throws SQLException {
        //3.获取用于向数据库发送sql语句的statement
        Statement st = c.createStatement();
        String sql = String.format("select * from paper where name='%s'",papername);
        ResultSet rs=st.executeQuery(sql);


        ArrayList<String> map=new ArrayList<>();
        while (rs.next())
        {
            String name = rs.getString("name");
            String reala ="A选项：" + rs.getString("a") + "|B选项："+rs.getString("b") + "|C选项：" + rs.getString("c") +
                    "|D选项：" + rs.getString("d") + "|正确答案：" +rs.getString("reala");
            map.add(name);
            map.add(reala);
        }
        System.out.println(map);
        return map;
    }

    public static void deletePaper(String paper) throws SQLException {
        PreparedStatement ps;
        //3.获取用于向数据库发送sql语句的statement
        Statement st = c.createStatement();
        String sql = String.format("delete from paper where name='%s'",paper);
        ps = c.prepareStatement(sql);
        ps.executeUpdate();
        ps.close();
        st.close();
    }

    public static ArrayList<String> selectPaperName(String knowledge) throws SQLException {
        Statement st = c.createStatement();
        String sql = String.format("select * from paper where knowledge='%s'",knowledge);
        ResultSet rs=st.executeQuery(sql);
        ArrayList<String> arrayList = new ArrayList<String>();
        while (rs.next()){
            arrayList.add(rs.getString("name"));
        }
        return arrayList;
    }

    public static String resetPassWord(String id, String old_pwd, String new_pwd) throws SQLException {
        Statement st = c.createStatement();
        String sql = String.format("select * from user where Number='%s' and password='%s'",id,old_pwd);
        ResultSet rs=st.executeQuery(sql);
        if (rs.next()){
            sql = String.format("update user set password='%s' where Number = '%s'",new_pwd,id);
            PreparedStatement ps = c.prepareStatement(sql);
            ps.executeUpdate();
            return "修改成功！";
        }
        return "旧密码错误，请重试！";
    }
}
