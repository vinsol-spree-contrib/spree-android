package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vaibhav on 11/17/15.
 */
public class BannerTypes implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("presentation")
    @Expose
    private String presentation;

    @SerializedName("banners")
    @Expose
    private ArrayList<Banner> banners;

    public BannerTypes() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public ArrayList<Banner> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<Banner> banners) {
        this.banners = banners;
    }
}
