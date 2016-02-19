package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by vaibhav on 10/5/15.
 */
//@Parcel
public class Taxonomy implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("pretty_name")
    @Expose
    private String prettyName;

    @SerializedName("permalink")
    @Expose
    private String permaLink;

    @SerializedName("root")
    @Expose
    private Taxon rootTaxon;

    public Taxonomy(Integer id, String name, String prettyName, String permaLink, Taxon rootTaxon) {
        this.id = id;
        this.name = name;
        this.prettyName = prettyName;
        this.permaLink = permaLink;
        this.rootTaxon = rootTaxon;
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

    public String getPrettyName() {
        return prettyName;
    }

    public void setPrettyName(String prettyName) {
        this.prettyName = prettyName;
    }

    public String getPermaLink() {
        return permaLink;
    }

    public void setPermaLink(String permaLink) {
        this.permaLink = permaLink;
    }

    public Taxon getRootTaxon() {
        return rootTaxon;
    }

    public void setRootTaxon(Taxon rootTaxon) {
        this.rootTaxon = rootTaxon;
    }
}
