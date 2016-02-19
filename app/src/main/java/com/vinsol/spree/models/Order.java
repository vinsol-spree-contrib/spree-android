package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vaibhav on 12/22/15.
 */
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("existing_card")
    @Expose
    private Integer existingCard;

    @SerializedName("number")
    @Expose
    private String number;

    @SerializedName("item_total")
    @Expose
    private String itemTotal;

    @SerializedName("total")
    @Expose
    private String total;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("adjustment_total")
    @Expose
    private String adjustmentTotal;

    @SerializedName("user_id")
    @Expose
    private Integer userId;

    @SerializedName("completed_at")
    @Expose
    private String completedAt;

    @SerializedName("ship_address_id")
    @Expose
    private Integer shipAddressId;

    @SerializedName("bill_address_id")
    @Expose
    private Integer billAddressId;

    @SerializedName("payment_total")
    @Expose
    private String paymentTotal;

    @SerializedName("shipment_state")
    @Expose
    private String shipmentState;

    @SerializedName("payment_state")
    @Expose
    private String paymentState;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("special_instructions")
    @Expose
    private String specialInstructions;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("shipping_method_id")
    @Expose
    private Integer shippingMethodId;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("last_ip_address")
    @Expose
    private String lastIpAddress;

    @SerializedName("created_by_id")
    @Expose
    private Integer createdById;

    @SerializedName("shipment_total")
    @Expose
    private String shipmentTotal;

    @SerializedName("additional_tax_total")
    @Expose
    private String additionalTaxTotal;

    @SerializedName("promo_total")
    @Expose
    private String promoTotal;

    @SerializedName("channel")
    @Expose
    private String channel;

    @SerializedName("included_tax_total")
    @Expose
    private String includedTaxTotal;

    @SerializedName("item_count")
    @Expose
    private Integer itemCount;

    @SerializedName("approver_id")
    @Expose
    private Integer approverId;

    @SerializedName("approved_at")
    @Expose
    private String approvedAt;

    @SerializedName("confirmation_delivered")
    @Expose
    private Boolean delivered;

    @SerializedName("considered_risky")
    @Expose
    private Boolean risky;

    @SerializedName("guest_token")
    @Expose
    private String guestToken;

    @SerializedName("checkout_steps")
    @Expose
    private ArrayList<String> checkoutSteps = new ArrayList<>();

    @SerializedName("errors")
    @Expose
    private ArrayList<String> errors = new ArrayList<>();

    @SerializedName("shipments")
    @Expose
    private ArrayList<Shipment> shipments = new ArrayList<>();

    @SerializedName("payments")
    @Expose
    private ArrayList<Payment> payments = new ArrayList<>();

    @SerializedName("line_items")
    @Expose
    private ArrayList<LineItem> lineItems = new ArrayList<>();

    @SerializedName("bill_address")
    @Expose
    private Address billingAddress;

    @SerializedName("ship_address")
    @Expose
    private Address shippingAddress;

    @SerializedName("bill_address_attributes")
    @Expose
    private Address billingAddressToBeAdded;

    @SerializedName("ship_address_attributes")
    @Expose
    private Address shippingAddressToBeAdded;

    public Order() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(String itemTotal) {
        this.itemTotal = itemTotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAdjustmentTotal() {
        return adjustmentTotal;
    }

    public void setAdjustmentTotal(String adjustmentTotal) {
        this.adjustmentTotal = adjustmentTotal;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public Integer getShipAddressId() {
        return shipAddressId;
    }

    public void setShipAddressId(Integer shipAddressId) {
        this.shipAddressId = shipAddressId;
    }

    public Integer getBillAddressId() {
        return billAddressId;
    }

    public void setBillAddressId(Integer billAddressId) {
        this.billAddressId = billAddressId;
    }

    public String getPaymentTotal() {
        return paymentTotal;
    }

    public void setPaymentTotal(String paymentTotal) {
        this.paymentTotal = paymentTotal;
    }

    public String getShipmentState() {
        return shipmentState;
    }

    public void setShipmentState(String shipmentState) {
        this.shipmentState = shipmentState;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getShippingMethodId() {
        return shippingMethodId;
    }

    public void setShippingMethodId(Integer shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLastIpAddress() {
        return lastIpAddress;
    }

    public void setLastIpAddress(String lastIpAddress) {
        this.lastIpAddress = lastIpAddress;
    }

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    public String getShipmentTotal() {
        return shipmentTotal;
    }

    public void setShipmentTotal(String shipmentTotal) {
        this.shipmentTotal = shipmentTotal;
    }

    public String getAdditionalTaxTotal() {
        return additionalTaxTotal;
    }

    public void setAdditionalTaxTotal(String additionalTaxTotal) {
        this.additionalTaxTotal = additionalTaxTotal;
    }

    public String getPromoTotal() {
        return promoTotal;
    }

    public void setPromoTotal(String promoTotal) {
        this.promoTotal = promoTotal;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getIncludedTaxTotal() {
        return includedTaxTotal;
    }

    public void setIncludedTaxTotal(String includedTaxTotal) {
        this.includedTaxTotal = includedTaxTotal;
    }

    public Integer getItemCount() {
        return itemCount;
    }

    public void setItemCount(Integer itemCount) {
        this.itemCount = itemCount;
    }

    public Integer getApproverId() {
        return approverId;
    }

    public void setApproverId(Integer approverId) {
        this.approverId = approverId;
    }

    public String getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(String approvedAt) {
        this.approvedAt = approvedAt;
    }

    public Boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Boolean isRisky() {
        return risky;
    }

    public void setRisky(Boolean risky) {
        this.risky = risky;
    }

    public String getGuestToken() {
        return guestToken;
    }

    public void setGuestToken(String guestToken) {
        this.guestToken = guestToken;
    }

    public ArrayList<String> getCheckoutSteps() {
        return checkoutSteps;
    }

    public void setCheckoutSteps(ArrayList<String> checkoutSteps) {
        this.checkoutSteps = checkoutSteps;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<String> errors) {
        this.errors = errors;
    }

    public ArrayList<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(ArrayList<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public ArrayList<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(ArrayList<Shipment> shipments) {
        this.shipments = shipments;
    }

    public void addLineItemToOrder(LineItem lineItem) {
        lineItems.add(lineItem);
    }

    public Address getBillingAddressToBeAdded() {
        return billingAddressToBeAdded;
    }

    public void setBillingAddressToBeAdded(Address billingAddressToBeAdded) {
        this.billingAddressToBeAdded = billingAddressToBeAdded;
    }

    public Address getShippingAddressToBeAdded() {
        return shippingAddressToBeAdded;
    }

    public void setShippingAddressToBeAdded(Address shippingAddressToBeAdded) {
        this.shippingAddressToBeAdded = shippingAddressToBeAdded;
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<Payment> payments) {
        this.payments = payments;
    }

    public Boolean getRisky() {
        return risky;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public Integer getExistingCard() {
        return existingCard;
    }

    public void setExistingCard(Integer existingCard) {
        this.existingCard = existingCard;
    }
}
