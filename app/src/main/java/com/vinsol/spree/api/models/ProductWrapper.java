package com.vinsol.spree.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.models.Product;

import java.io.Serializable;

//@Parcel
public class ProductWrapper implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("product")
    @Expose
    private Product product;

    public ProductWrapper(){}

    /**
     *
     * @return
     *     The product
     */
    public Product getProduct() {
        return product == null ? new Product() : product;
    }
}
