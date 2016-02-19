package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 10/5/15.
 */
//@Parcel
public class Taxon implements Serializable {
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

    @SerializedName("icon_url")
    @Expose
    private String iconUrl;

    @SerializedName("parent_id")
    @Expose
    private Integer parentId;

    @SerializedName("taxonomy_id")
    @Expose
    private Integer taxonomyId;

    @SerializedName("children")
    @Expose
    private List<Taxon> children;

    public Taxon() {
        children = new ArrayList<Taxon>();
    }

    public Taxon(Integer id, String name, String prettyName, String permaLink, Integer parentId, Integer taxonomyId, List<Taxon> children) {
        this.id = id;
        this.name = name;
        this.prettyName = prettyName;
        this.permaLink = permaLink;
        this.parentId = parentId;
        this.taxonomyId = taxonomyId;
        this.children = children;
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

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getTaxonomyId() {
        return taxonomyId;
    }

    public void setTaxonomyId(Integer taxonomyId) {
        this.taxonomyId = taxonomyId;
    }

    public List<Taxon> getChildren() {
        return children;
    }

    public void setChildren(List<Taxon> children) {
        this.children = children;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
