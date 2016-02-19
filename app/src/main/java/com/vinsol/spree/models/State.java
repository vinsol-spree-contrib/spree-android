package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vaibhav on 12/21/15.
 */
public class State implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("abbr")
    @Expose
    private String abbr;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private Integer id;

    public State() {
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
