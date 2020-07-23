package com.rdv.slcard.API;

import java.util.HashMap;
import java.util.Map;

public class ResultLogin {

    private String state;
    private Map<String,String> user = new HashMap<>();
    private Map <String,String> errors = new HashMap<>();

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Map<String, String> getUser() {
        return user;
    }

    public void setUser(Map<String, String> user) {
        this.user = user;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
