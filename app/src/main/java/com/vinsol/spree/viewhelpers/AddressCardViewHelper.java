package com.vinsol.spree.viewhelpers;

import android.view.View;
import android.widget.TextView;

import com.vinsol.spree.R;
import com.vinsol.spree.models.Address;

public class AddressCardViewHelper {
    private View parentView;
    private TextView addressTxt;
    private TextView phoneTxt;

    public AddressCardViewHelper(View parentView) {
        this.parentView = parentView;
    }

    public void setUpCard(Address address) {
        addressTxt = (TextView) parentView.findViewById(R.id.cart_address_item_txt);
        phoneTxt = (TextView) parentView.findViewById(R.id.cart_address_item_phone_txt);

        if (address != null) {
            String add = address.getFirstName()
                    + " " + address.getLastName()
                    + " \n " + address.getAddressLine1() + ", "
                    + address.getAddressLine2() + ", "
                    + address.getCity() + ", "
                    + address.getStateName() + ", "
                    + address.getZipcode();
            addressTxt.setText(add);
            phoneTxt.setText(address.getPhone());
        } else {
            addressTxt.setText("");
            phoneTxt.setText("");
        }
    }

    public String getAddress() {
        return addressTxt.getText().toString();
    }
}
