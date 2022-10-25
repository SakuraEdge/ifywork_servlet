package com.ifywork.system.pojo;

public abstract class Person {
    private String name;
    private String sex;
    private String mailbox;
    private String tel;
    private String num;
    private int type;
    private String nickname;
    private String password;

    public Person(String  num, String name, String sex, String tel, String nickname, String mailbox, String password, int type)
    {
        this.num = num;
        this.name = name;
        this.sex = sex;
        this.mailbox = mailbox;
        this.password = password;
        this.tel = tel;
        this.nickname = nickname;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTel(){return tel;}

    public String getNickname(){return nickname;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum(){return num;}

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMailbox() {
        return mailbox;
    }

    public void setMailbox(String mailbox) {
        this.mailbox = mailbox;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void show()
    {
        System.out.println("******个人信息展示******");
        System.out.println("姓名：" + name + "\n昵称：" + nickname +  "\n性别：" + sex + "\n学号/工号：" + num
                + "\n电话：" + tel + "\n邮箱：" + mailbox);
    }
}
