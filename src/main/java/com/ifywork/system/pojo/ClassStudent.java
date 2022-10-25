package com.ifywork.system.pojo;

public class ClassStudent {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String name;
    private String id;

    public ClassStudent(String name, String id) {
        this.name = name;
        this.id = id;
    }

}
