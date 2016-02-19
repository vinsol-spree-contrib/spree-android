package com.vinsol.spree.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.models.Order;


import java.io.Serializable;

/**
 * Created by vaibhav on 12/28/15.
 */
public class OrderWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("order")
    @Expose
    private Order order;

    public OrderWrapper(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
