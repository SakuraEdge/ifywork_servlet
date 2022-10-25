package com.ifywork.system.pojo;

public class MyClass {
    private String name;
    private String teacherID;
    private int maxNum;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MyClass(String name, String teacherID, int maxNum, String address) {
        this.name = name;
        this.teacherID = teacherID;
        this.maxNum = maxNum;
        this.address = address;
    }
}
