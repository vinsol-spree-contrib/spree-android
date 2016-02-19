package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vaibhav on 12/3/15.
 */
public class Image implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("position")
    @Expose
    private Integer position;

    @SerializedName("mini_url")
    @Expose
    private String miniUrl;

    @SerializedName("small_url")
    @Expose
    private String smallUrl;

    @SerializedName("product_url")
    @Expose
    private String productUrl;

    @SerializedName("large_url")
    @Expose
    private String largeUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getMiniUrl() {
        return miniUrl;
    }

    public void setMiniUrl(String miniUrl) {
        this.miniUrl = miniUrl;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getLargeUrl() {
        return largeUrl;
    }

    public void setLargeUrl(String largeUrl) {
        this.largeUrl = largeUrl;
    }
}
