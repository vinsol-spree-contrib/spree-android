package com.vinsol.spree.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.models.Product;
import com.vinsol.spree.models.Variant;

//import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 10/5/15.
 */
//@Parcel
public class VariantsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @Expose
    private List<Variant> variants = new ArrayList<Variant>();

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    @SerializedName("current_page")
    @Expose
    private Integer currentPage;

    @SerializedName("pages")
    @Expose
    private Integer pages;

    public VariantsResponse(){}

    /**
     *
     * @return
     *     The variants
     */
    public List<Variant> getVariants() {
        return variants;
    }

    /**
     *
     * @param variants
     *
     */
    public void setVariants(List<Variant> variants) {
        this.variants = variants;
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
     * @return totalCount
     *
     */
    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     *
     * @param totalCount
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
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
}
