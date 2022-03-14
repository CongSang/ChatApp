package com.user;

import java.io.Serializable;

public class User implements Serializable {
    public String picture;
    public String firstName;
    public String lastName;
    public String email;
    public String phone;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return lastName;
    }

    public void setName(String name) {
        this.lastName = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
