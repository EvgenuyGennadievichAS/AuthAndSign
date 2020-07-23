package com.rdv.slcard.API;

import java.util.HashMap;
import java.util.Map;

public class ResultRegister {
    private String state;
    private Map<String,String> errors = new HashMap<String,String>();

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }


}
