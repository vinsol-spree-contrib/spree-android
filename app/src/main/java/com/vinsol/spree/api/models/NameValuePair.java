package com.vinsol.spree.api.models;

/**
 * Created by vaibhav on 12/16/15.
 */
public class NameValuePair {
    //TODO: to be deleted if QueryMap supports multivaluemap
    private String name;
    private String value;

    public NameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
