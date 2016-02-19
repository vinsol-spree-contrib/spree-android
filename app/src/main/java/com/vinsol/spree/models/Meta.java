package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vaibhav on 10/20/15.
 */
public class Meta implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
