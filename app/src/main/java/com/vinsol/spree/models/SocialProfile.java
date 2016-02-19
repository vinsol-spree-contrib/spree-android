package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vaibhav on 12/1/15.
 */
public class SocialProfile implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("uid")
    @Expose
    private String uid;

    @SerializedName("provider")
    @Expose
    private String provider;

    @SerializedName("user_id")
    @Expose
    private Integer userId;

    @SerializedName("profile_pic_url")
    @Expose
    private String image;

    public SocialProfile(String provider, String uid, String image) {
        this.provider = provider;
        this.uid = uid;
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
