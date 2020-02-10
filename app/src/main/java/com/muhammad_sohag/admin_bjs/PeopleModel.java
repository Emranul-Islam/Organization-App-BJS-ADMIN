package com.muhammad_sohag.admin_bjs;

public class PeopleModel {
    private String image;
    private String name;
    private String id;
    private String number;


    public PeopleModel(String image, String name,String id,String number) {
        this.image = image;
        this.name = name;
        this.id = id;
        this.number = number;
    }


    public PeopleModel(){}

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}


