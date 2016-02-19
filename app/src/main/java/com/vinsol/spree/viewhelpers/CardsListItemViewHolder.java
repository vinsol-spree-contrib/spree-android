package com.vinsol.spree.viewhelpers;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vinsol.spree.R;
import com.vinsol.spree.SpreeApplication;

import com.vinsol.spree.events.CardsListOptionClickEvent;
import com.vinsol.spree.models.Card;
import com.vinsol.spree.utils.BusProvider;

/**
 * Created by vaibhav on 12/24/15.
 */
public class CardsListItemViewHolder {
    private TextView cardType, cardNumber, expDate;
    private RelativeLayout moreOptions;
    private ImageView tick;

    public CardsListItemViewHolder(View view) {
        cardType    = (TextView)  view.findViewById(R.id.cards_list_view_item_cc_type);
        cardNumber  = (TextView)  view.findViewById(R.id.cards_list_view_item_number);
        expDate     = (TextView)  view.findViewById(R.id.cards_list_view_item_date);
        moreOptions = (RelativeLayout) view.findViewById(R.id.cards_list_view_item_option);
        tick        = (ImageView) view.findViewById(R.id.cards_list_view_item_selected);
    }

    public void setup(final Card card) {
        cardType.setText(card.getType());
        cardNumber.setText("**** **** **** " + card.getLastDigits());
        expDate.setText(card.getMonth() + " / " + card.getYear());
        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(SpreeApplication.getContext(), v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.cards_list_item_popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        BusProvider.getInstance().post(new CardsListOptionClickEvent(card));
                        return true;
                    }
                });

                popup.show();
            }
        });
        tick.setVisibility(View.GONE);
        if (card.isSelected()) tick.setVisibility(View.VISIBLE);
    }
}
