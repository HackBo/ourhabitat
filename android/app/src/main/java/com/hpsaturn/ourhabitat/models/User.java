package com.hpsaturn.ourhabitat.models;

import android.graphics.Bitmap;

/**
 * Created by Antonio Vanegas @hpsaturn on 11/5/15.
 */
public class User {

    public String username;

    public String phone;

    public Bitmap photo;

    public User(String name, String phone, Bitmap photo) {
        this.username =name;
        this.phone=phone;
        this.photo=photo;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "username: "+ username +" phone:"+phone;
    }

    public String getPhone() {
        return phone;
    }

    public Bitmap getPhoto() {
        return photo;
    }
}
