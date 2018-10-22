package com.software.abiol.simesapp.models;

public class Executives {
    private String name;



    private String first_name;
    private String last_name;
    private String image;
    private String position;
    private String phone_number;

    public Executives(){

    }

    public Executives(String name, String first_name, String last_name, String image,  String position, String phone_number) {
        this.name = name;
        this.first_name = first_name;
        this.last_name = last_name;
        this.image = image;
        this.position = position;
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }


}
