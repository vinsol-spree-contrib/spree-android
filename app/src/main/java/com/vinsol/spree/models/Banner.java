package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vaibhav on 11/3/15.
 */
public class Banner implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("image_url")
    @Expose
    private String image;

    @SerializedName("target_url")
    @Expose
    private String targetUrl;

    public Banner() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
}
