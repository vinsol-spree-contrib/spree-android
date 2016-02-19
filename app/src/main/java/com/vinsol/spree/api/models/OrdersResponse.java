package com.vinsol.spree.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.models.Filter;
import com.vinsol.spree.models.Order;
import com.vinsol.spree.models.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 12/30/15.
 */
public class OrdersResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("orders")
    @Expose
    private List<Order> orders = new ArrayList<Order>();

    @SerializedName("count")
    @Expose
    private Integer count;

    @SerializedName("current_page")
    @Expose
    private Integer currentPage;

    @SerializedName("pages")
    @Expose
    private Integer pages;


    public OrdersResponse(){}

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }
}
