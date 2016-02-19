package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vaibhav on 12/22/15.
 */
public class LineItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("single_display_amount")
    @Expose
    private String singleDisplayAmount;

    @SerializedName("display_amount")
    @Expose
    private String displayAmount;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("insufficient_stock")
    @Expose
    private Boolean insufficientStock;

    @SerializedName("variant")
    @Expose
    private Variant variant;

    @SerializedName("variant_id")
    @Expose
    private Integer variantId;

    public LineItem() {
    }

    public LineItem(Integer quantity, Integer variantId) {
        this.quantity = quantity;
        this.variantId = variantId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSingleDisplayAmount() {
        return singleDisplayAmount;
    }

    public void setSingleDisplayAmount(String singleDisplayAmount) {
        this.singleDisplayAmount = singleDisplayAmount;
    }

    public String getDisplayAmount() {
        return displayAmount;
    }

    public void setDisplayAmount(String displayAmount) {
        this.displayAmount = displayAmount;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Boolean isInsufficientStock() {
        return insufficientStock;
    }

    public void setInsufficientStock(Boolean insufficientStock) {
        this.insufficientStock = insufficientStock;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }
}
