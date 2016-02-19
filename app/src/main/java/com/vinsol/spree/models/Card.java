package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vaibhav on 12/24/15.
 */
public class Card implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("last_digits")
    @Expose
    private String lastDigits;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("month")
    @Expose
    private Integer month;

    @SerializedName("year")
    @Expose
    private Integer year;

    @SerializedName("cc_type")
    @Expose
    private String type;

    @SerializedName("gateway_payment_profile_id")
    @Expose
    private String stripeTokenId;

    @SerializedName("gateway_customer_profile_id")
    @Expose
    private String customerProfileId;

    @SerializedName("number")
    @Expose
    private String number;

    @SerializedName("payment_method")
    @Expose
    private PaymentMethod paymentMethod;

    @SerializedName("cvv")
    @Expose
    private String cvv;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("default")
    @Expose
    private Boolean isDefault;

    private boolean isSelected = false;


    public Card() {
    }

    public Card(String lastDigits, Integer month, Integer year, String type) {
        this.lastDigits = lastDigits;
        this.month = month;
        this.year = year;
        this.type = type;
    }

    public String getLastDigits() {
        return lastDigits;
    }

    public void setLastDigits(String lastDigits) {
        this.lastDigits = lastDigits;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStripeTokenId() {
        return stripeTokenId;
    }

    public void setStripeTokenId(String stripeTokenId) {
        this.stripeTokenId = stripeTokenId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getCustomerProfileId() {
        return customerProfileId;
    }

    public void setCustomerProfileId(String customerProfileId) {
        this.customerProfileId = customerProfileId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
