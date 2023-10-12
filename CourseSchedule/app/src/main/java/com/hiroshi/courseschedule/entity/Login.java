package com.hiroshi.courseschedule.entity;

public class Login {

    private String name;
    private String tel;
    private String pass;

    public Login(String name, String tel, String pass) {
        this.name = name;
        this.tel = tel;
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
