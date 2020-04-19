package com.muhammad_sohag.admin_bjs.model;

public class PeopleModel {
    //  email name number password blood uid url
    private String email;
    private String name;
    private String number;
    private String password;
    private String password2;
    private String blood;
    private String uid;
    private String url;


    public PeopleModel(String email, String name, String number, String password,String password2, String blood, String uid, String url) {
        this.email = email;
        this.name = name;
        this.number = number;
        this.password = password;
        this.password2 = password2;
        this.blood = blood;
        this.uid = uid;
        this.url = url;
    }

    public PeopleModel(){}

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}


