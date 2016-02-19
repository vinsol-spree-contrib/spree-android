package com.vinsol.spree.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.models.Filter;
import com.vinsol.spree.models.Product;

//import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 9/29/15.
 */
//@Parcel
public class ProductsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("products")
    @Expose
    private List<Product> products = new ArrayList<Product>();

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;

    @SerializedName("current_page")
    @Expose
    private Integer currentPage;

    @SerializedName("per_page")
    @Expose
    private Integer perPage;

    @SerializedName("pages")
    @Expose
    private Integer pages;

    @SerializedName("filters")
    @Expose
    private List<Filter> filters = new ArrayList<>();

    public ProductsResponse(){}

    /**
     *
     * @return
     *     The products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     *
     * @param products
     *
     */
    public void setProducts(List<Product> products) {
        this.products = products;
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
     * @return perPage
     *
     */
    public Integer getPerPage() {
        return perPage;
    }

    /**
     *
     * @param perPage
     *
     */
    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
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

    /**
     *
     * @return
     *     The filters
     */
    public List<Filter> getFilters() {
        return filters;
    }

    /**
     *
     * @param filters
     *
     */
    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
}
