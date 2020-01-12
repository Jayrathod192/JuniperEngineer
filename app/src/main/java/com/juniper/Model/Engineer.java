package com.juniper.Model;

public class Engineer {

    private String id, name, phone, engname, image, education, profession,rate,type;

    public Engineer() {
    }

    public Engineer(String id ,String name, String phone, String engname, String image, String education, String profession,String rate,String type) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.engname = engname;
        this.image = image;
        this.education = education;
        this.profession = profession;
        this.rate=rate;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRate() {
        return rate;
    }
    public void setRate(String rate) {
        this.rate = rate;
    }
    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEngname() {
        return engname;
    }

    public void setEngname(String engname) {
        this.engname = engname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}

