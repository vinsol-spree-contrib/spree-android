package com.vinsol.spree.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vinsol.spree.utils.Constants;
import com.vinsol.spree.utils.SharedPreferencesHelper;

import java.io.Serializable;

/**
 * Created by vaibhav on 12/21/15.
 */
public class Address implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("firstname")
    @Expose
    private String firstName;

    @SerializedName("lastname")
    @Expose
    private String lastName;

    @SerializedName("address1")
    @Expose
    private String addressLine1;

    @SerializedName("address2")
    @Expose
    private String addressLine2;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("zipcode")
    @Expose
    private String zipcode;

    @SerializedName("state_name")
    @Expose
    private String stateName;

    @SerializedName("alternative_phone")
    @Expose
    private String alternativePhone;

    @SerializedName("company")
    @Expose
    private String company;

    @SerializedName("state_id")
    @Expose
    private Integer stateId;

    @SerializedName("country_id")
    @Expose
    private Integer countryId;

    private boolean isSelected = false;

    public Address() {
        countryId = Constants.COUNTRY_ID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getStateName() {
        if (stateId!=null) {
            for (State state :
                    SharedPreferencesHelper.getCache().getStatesList()) {
                if (state.getId().equals(stateId)) return state.getName();
            }
        }
        return stateName==null?"":stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getAlternativePhone() {
        return alternativePhone;
    }

    public void setAlternativePhone(String alternativePhone) {
        this.alternativePhone = alternativePhone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Integer getCountryId() {
        return Constants.COUNTRY_ID;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = Constants.COUNTRY_ID;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
