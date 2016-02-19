package com.vinsol.spree.events;

import com.vinsol.spree.models.Address;

/**
 * Created by vaibhav on 12/21/15.
 */
public class AddressesListOptionClickEvent {
    public String operationType;
    public Address address;

    public AddressesListOptionClickEvent(String operationType, Address address) {
        this.operationType = operationType;
        this.address = address;
    }
}
