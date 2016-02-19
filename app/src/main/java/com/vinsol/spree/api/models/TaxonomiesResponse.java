package com.vinsol.spree.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.models.Meta;
import com.vinsol.spree.models.Taxonomy;
import com.vinsol.spree.utils.Strings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 10/5/15.
 */

public class TaxonomiesResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @Expose
    private List<Taxonomy> taxonomies = new ArrayList<Taxonomy>();

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("current_page")
    @Expose
    private Integer currentPage;

    @SerializedName("pages")
    @Expose
    private Integer pages;

    @SerializedName("meta")
    @Expose
    private Meta meta;

    @SerializedName("checksum")
    @Expose
    private String checksum;

    public TaxonomiesResponse(){}

    /**
     *
     * @return
     *     The taxonomies
     */
    public List<Taxonomy> getTaxonomies() {
        return taxonomies;
    }

    /**
     *
     * @param taxonomies
     *
     */
    public void setTaxonomies(List<Taxonomy> taxonomies) {
        this.taxonomies = taxonomies;
    }

    /**
     *
     * @return count
     *
     */
    public Integer getCount() {
        return count;
    }

    /**
     *
     * @param count
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     *
     * @return currentPage
     *
     */
    public Integer getCurrentPage() {
        return currentPage;
    }

    /**
     *
     * @param currentPage
     *
     */
    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    /**
     *
     * @return pages
     *
     */
    public Integer getPages() {
        return pages;
    }

    /**
     *
     * @param pages
     *
     */
    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public String getChecksum() {
        return Strings.nullSafeString(checksum);
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
