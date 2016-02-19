package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vaibhav on 11/30/15.
 */
public class Errors implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("email")
    @Expose
    private ArrayList<String> email;

    @SerializedName("password")
    @Expose
    private ArrayList<String> password;

    @SerializedName("current_password")
    @Expose
    private ArrayList<String> currentPasswords;

    @SerializedName("zipCode")
    @Expose
    private ArrayList<String> zipCode;

    public ArrayList<String> getEmail() {
        return email;
    }

    public void setEmail(ArrayList<String> email) {
        this.email = email;
    }

    public ArrayList<String> getPassword() {
        return password;
    }

    public void setPassword(ArrayList<String> password) {
        this.password = password;
    }

    public ArrayList<String> getCurrentPasswords() {
        return currentPasswords;
    }

    public void setCurrentPasswords(ArrayList<String> currentPasswords) {
        this.currentPasswords = currentPasswords;
    }

    public ArrayList<String> getZipCode() {
        return zipCode;
    }

    public void setZipCode(ArrayList<String> zipCode) {
        this.zipCode = zipCode;
    }
}
