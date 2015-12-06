package com.hpsaturn.ourhabitat.models;

import java.util.List;

/**
 * Created by Antonio Vanegas @hpsaturn on 11/5/15.
 */
public class Ring {

    public String name;

    public String description;

    public boolean enable=true;

    public List<User> contacts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
    }

    public List<User> getContacts(){return contacts;}

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }


}
