package com.vinsol.spree.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.models.ProductProperty;
import com.vinsol.spree.models.Variant;

//import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 10/5/15.
 */
//@Parcel
public class ProductPropertiesResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @Expose
    private List<ProductProperty> productProperties = new ArrayList<ProductProperty>();

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("current_page")
    @Expose
    private Integer currentPage;

    @SerializedName("pages")
    @Expose
    private Integer pages;

    public ProductPropertiesResponse(){}

    /**
     *
     * @return
     *     The productProperties
     */
    public List<ProductProperty> getProductProperties() {
        return productProperties;
    }

    /**
     *
     * @param productProperties
     *
     */
    public void setProductProperties(List<ProductProperty> productProperties) {
        this.productProperties = productProperties;
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
}
