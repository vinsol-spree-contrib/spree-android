package com.vinsol.spree.viewhelpers;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vinsol.spree.R;
import com.vinsol.spree.SpreeApplication;
import com.vinsol.spree.events.AddressesListOptionClickEvent;
import com.vinsol.spree.models.Address;
import com.vinsol.spree.utils.BusProvider;

/**
 * Created by vaibhav on 12/21/15.
 */
public class AddressesListItemViewHolder {
    private TextView name, address, phone;
    private RelativeLayout moreOptions;
    private ImageView tick;

    public AddressesListItemViewHolder(View view) {
        name        = (TextView)  view.findViewById(R.id.addresses_list_view_item_name);
        address     = (TextView)  view.findViewById(R.id.addresses_list_view_item_address);
        phone       = (TextView)  view.findViewById(R.id.addresses_list_view_item_phone);
        moreOptions = (RelativeLayout) view.findViewById(R.id.addresses_list_view_item_option);
        tick        = (ImageView) view.findViewById(R.id.addresses_list_view_item_selected);
    }

    public void setup(final Address address) {
        name.setText(address.getFirstName() + " " + address.getLastName());
        this.address.setText(address.getAddressLine1() + ", \n" + address.getAddressLine2() + ", \n"
        + address.getCity() + ", " + address.getStateName() + " \n " + address.getZipcode());
        phone.setText(address.getPhone());
        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(SpreeApplication.getContext(), v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.addresses_list_item_popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        BusProvider.getInstance().post(new AddressesListOptionClickEvent(item.getTitle().toString(), address));
                        return true;
                    }
                });

                popup.show();
            }
        });
        tick.setVisibility(View.GONE);
        if (address.isSelected()) tick.setVisibility(View.VISIBLE);
    }
}
