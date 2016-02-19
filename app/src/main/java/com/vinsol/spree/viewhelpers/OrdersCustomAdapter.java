package com.vinsol.spree.viewhelpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vinsol.spree.R;
import com.vinsol.spree.models.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 12/30/15.
 */
public class OrdersCustomAdapter extends RecyclerView.Adapter<OrdersListItemViewHolder> {
    private Context context;
    private List<Order> orders;

    public OrdersCustomAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @Override
    public OrdersListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrdersListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_list_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(OrdersListItemViewHolder holder, int position) {
        holder.setup(orders.get(position));
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }
}
