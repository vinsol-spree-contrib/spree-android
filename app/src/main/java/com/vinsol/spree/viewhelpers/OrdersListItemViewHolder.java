package com.vinsol.spree.viewhelpers;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vinsol.spree.R;
import com.vinsol.spree.SpreeApplication;
import com.vinsol.spree.models.LineItem;
import com.vinsol.spree.models.Order;
import com.vinsol.spree.utils.Common;
import com.vinsol.spree.utils.StaticSingletonCustomFonts;
import com.vinsol.spree.views.CustomFontTextView;

/**
 * Created by vaibhav on 11/18/15.
 */
public class OrdersListItemViewHolder extends RecyclerView.ViewHolder {
    private TextView shipmentPrice, taxPrice, orderNumber, shimentStatus;
    private TextView orderedOn, totalItems, totalPrice;
    private LinearLayout lineItemsView;

    public OrdersListItemViewHolder(View itemView) {
        super(itemView);
        orderedOn       = (TextView)    itemView.findViewById(R.id.orders_list_view_item_ordered_on_txt);
        totalItems      = (TextView)    itemView.findViewById(R.id.orders_list_view_item_total_items_txt);
        totalPrice      = (TextView)    itemView.findViewById(R.id.orders_list_view_item_total_price_txt);
        shipmentPrice   = (TextView)    itemView.findViewById(R.id.orders_list_view_item_shipment_price);
        taxPrice        = (TextView)    itemView.findViewById(R.id.orders_list_view_item_tax_price);
        orderNumber     = (TextView)    itemView.findViewById(R.id.orders_list_view_item_order_number_txt);
        shimentStatus   = (TextView)    itemView.findViewById(R.id.orders_list_view_item_shipment_status_txt);
        lineItemsView   = (LinearLayout)itemView.findViewById(R.id.orders_list_view_item_line_items_container);
    }

    public void setup(Order order) {
        orderedOn.setText(order.getCompletedAt());
        totalItems.setText(String.valueOf(order.getItemCount()));
        totalPrice.setText("$ "+order.getTotal());
        shipmentPrice.setText("$ " + order.getShipmentTotal());
        taxPrice.setText("$ " + order.getAdditionalTaxTotal());
        orderNumber.setText(" " + order.getId());
        shimentStatus.setText(" " + order.getShipmentState());
        lineItemsView.removeAllViews();
        for (LineItem li :
                order.getLineItems()) {
            createAndAddViewForLineItem(li);
        }
    }

    private void createAndAddViewForLineItem(LineItem lineItem) {
        LinearLayout row = getRowLayout();
        TextView name = getNameTextView();
        name.setText(lineItem.getVariant().getName());
        row.addView(name);
        TextView quantity = getQuantityTextView();
        quantity.setText(String.valueOf(lineItem.getQuantity()));
        row.addView(quantity);
        TextView price = getPriceTextView();
        price.setText("$ " + lineItem.getPrice());
        row.addView(price);
        lineItemsView.addView(row);
    }

    private LinearLayout getRowLayout() {
        LinearLayout row = new LinearLayout(SpreeApplication.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        row.setOrientation(LinearLayout.HORIZONTAL);
        layoutParams.bottomMargin = Common.convertDpToPixel(SpreeApplication.getContext(), 10);
        row.setLayoutParams(layoutParams);
        return row;
    }

    private CustomFontTextView getNameTextView() {
        CustomFontTextView customFontTextView = new CustomFontTextView(SpreeApplication.getContext());
        customFontTextView.setTypeface(StaticSingletonCustomFonts.getTypeface("roboto_light.ttf"));
        customFontTextView.setTextColor(SpreeApplication.getContext().getResources().getColor(R.color.textColor));
        customFontTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, SpreeApplication.getContext().getResources().getDimension(R.dimen.text_size_12sp));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 10;
        customFontTextView.setLayoutParams(layoutParams);
        return customFontTextView;
    }

    private CustomFontTextView getQuantityTextView() {
        CustomFontTextView customFontTextView = new CustomFontTextView(SpreeApplication.getContext());
        customFontTextView.setTypeface(StaticSingletonCustomFonts.getTypeface("roboto_light.ttf"));
        customFontTextView.setTextColor(SpreeApplication.getContext().getResources().getColor(R.color.textColor));
        customFontTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, SpreeApplication.getContext().getResources().getDimension(R.dimen.text_size_12sp));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        customFontTextView.setLayoutParams(layoutParams);
        return customFontTextView;
    }


    private CustomFontTextView getPriceTextView() {
        CustomFontTextView customFontTextView = new CustomFontTextView(SpreeApplication.getContext());
        customFontTextView.setTypeface(StaticSingletonCustomFonts.getTypeface("roboto_medium.ttf"));
        customFontTextView.setTextColor(SpreeApplication.getContext().getResources().getColor(R.color.textColor));
        customFontTextView.setGravity(Gravity.RIGHT);
        customFontTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, SpreeApplication.getContext().getResources().getDimension(R.dimen.text_size_12sp));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 2;
        customFontTextView.setLayoutParams(layoutParams);
        return customFontTextView;
    }

}
