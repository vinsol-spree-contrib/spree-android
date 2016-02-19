package com.vinsol.spree.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.models.BannerTypes;
import com.vinsol.spree.utils.Strings;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vaibhav on 11/9/15.
 */
public class HomeResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("checksum")
    @Expose
    private String checksum;

    @SerializedName("banner_types")
    @Expose
    private ArrayList<BannerTypes> bannerTypes;

    public ArrayList<BannerTypes> getBannerTypes() {
        return bannerTypes;
    }

    public void setBannerTypes(ArrayList<BannerTypes> bannerTypes) {
        this.bannerTypes = bannerTypes;
    }

    public String getChecksum() {
        return Strings.nullSafeString(checksum);
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}


