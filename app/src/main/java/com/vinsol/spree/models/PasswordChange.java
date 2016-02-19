package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vaibhav on 12/22/15.
 */
public class PasswordChange implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("current_password")
    @Expose
    private String currentPwd;

    @SerializedName("password")
    @Expose
    private String pwd;

    public PasswordChange() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCurrentPwd() {
        return currentPwd;
    }

    public void setCurrentPwd(String currentPwd) {
        this.currentPwd = currentPwd;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
