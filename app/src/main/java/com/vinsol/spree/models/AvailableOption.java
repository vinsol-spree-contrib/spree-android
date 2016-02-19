package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


//@Parcel
public class AvailableOption implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("values")
    @Expose
    private ArrayList<String> values;

    private String currentlySelectedValue;


    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getValues() {
        return (values != null && values.size() > 0) ? values : new ArrayList<String>();
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public String getCurrentlySelectedValue() {
        return currentlySelectedValue == null ? "" : currentlySelectedValue;
    }

    public void setCurrentlySelectedValue(String currentlySelectedValue) {
        this.currentlySelectedValue = currentlySelectedValue;
    }
}
