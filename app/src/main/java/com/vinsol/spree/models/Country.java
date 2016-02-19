package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.utils.Strings;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vaibhav on 12/21/15.
 */
public class Country implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("iso")
    @Expose
    private String iso;

    @SerializedName("checksum")
    @Expose
    private String checksum;


    @SerializedName("states")
    @Expose
    private ArrayList<State> states = new ArrayList<>();

    public Country() {
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

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getChecksum() {
        return Strings.nullSafeString(checksum);
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public void setStates(ArrayList<State> states) {
        this.states = states;
    }
}
