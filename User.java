package com.rdv.slcard.Authentication;

public class User {
    private String email,password,name,suName,phone;

    public User(){}

    public User(String email, String password, String name, String suName, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.suName = suName;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuName() {
        return suName;
    }

    public void setSuName(String suName) {
        this.suName = suName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
