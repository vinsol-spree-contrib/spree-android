package com.vinsol.spree.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.models.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@Parcel
public class ProductsByTaxonsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("searches")
    @Expose
    private List<Product> products = new ArrayList<>();

//    @SerializedName("filters")
//    @Expose
//    private List<Filter> filters = new ArrayList<>();

    public ProductsByTaxonsResponse(){}

    /**
     *
     * @return
     *     The products
     */
    public List<Product> getProducts() {
        return products;
    }

//    /**
//     *
//     * @return
//     *     The filters
//     */
//    public List<Filter> getFilters() {
//        return filters;
//    }
}
