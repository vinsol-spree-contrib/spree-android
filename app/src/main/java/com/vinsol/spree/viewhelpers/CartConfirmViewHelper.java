package com.vinsol.spree.viewhelpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vinsol.spree.R;
import com.vinsol.spree.SpreeApplication;
import com.vinsol.spree.models.Address;
import com.vinsol.spree.models.LineItem;
import com.vinsol.spree.models.Order;
import com.vinsol.spree.models.Payment;

import java.util.ArrayList;

/**
 * Created by vaibhav on 1/3/16.
 */
public class CartConfirmViewHelper {
    protected Context context;

    protected LayoutInflater layoutInflater;
    protected LinearLayout parent;

    public CartConfirmViewHelper(Context context, LayoutInflater layoutInflater, LinearLayout parent) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.parent = parent;
    }

    public void createView(Order order) {
        createViewForLineItems(order.getLineItems());
        createViewForAddress(order.getShippingAddress());
        createViewForPayment(order.getPayments().get(0));
    }

    private void createViewForLineItems(ArrayList<LineItem> lineItems) {
        for (LineItem lineItem : lineItems) {
            View view               = layoutInflater.inflate(R.layout.line_items_list_view_item, parent, false);
            ImageView lineItemImage = (ImageView) view.findViewById(R.id.line_items_list_view_item_img);
            TextView lineItemName   = (TextView) view.findViewById(R.id.line_items_list_view_item_name);
            TextView lineItemQty    = (TextView) view.findViewById(R.id.line_items_list_view_item_qty_txt);
            TextView lineItemPrice  = (TextView) view.findViewById(R.id.line_items_list_view_item_price);

            // hide action bar
            View    actionBarDivider = view.findViewById(R.id.line_items_list_view_action_divider);
            actionBarDivider.setVisibility(View.GONE);
            LinearLayout actionsBar = (LinearLayout) view.findViewById(R.id.line_items_list_view_actions_bar);
            actionsBar.setVisibility(View.GONE);

            if (lineItem.getVariant().getImages()!=null && !lineItem.getVariant().getImages().isEmpty())
                Picasso.with(SpreeApplication.getContext()).load(lineItem.getVariant().getImages().get(0).getProductUrl()).into(lineItemImage);
            lineItemQty.setText("Qty. " + lineItem.getQuantity());
            lineItemName.setText(lineItem.getVariant().getName());
            lineItemPrice.setText("$ " + lineItem.getTotal());
            parent.addView(view);
        }
    }

    private void createViewForAddress(Address address) {
        View view = layoutInflater.inflate(R.layout.cart_address_item, parent, false);
        new AddressCardViewHelper(view).setUpCard(address);
        parent.addView(view);
    }

    private void createViewForPayment(Payment payment) {
        View view = layoutInflater.inflate(R.layout.cart_payment_item, parent, false);
        new PaymentCardViewHelper(view).setUpCard(payment);
        parent.addView(view);
    }
}
