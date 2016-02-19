package com.vinsol.spree.viewhelpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.vinsol.spree.R;
import com.vinsol.spree.models.LineItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 12/29/15.
 */
public class LineItemsCustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private CartLineItemActionsInterface actionsInterface;
    private List<LineItem> lineItems;

    public LineItemsCustomAdapter(Context context, CartLineItemActionsInterface actionsInterface, ArrayList<LineItem> lineItems) {
        this.context = context;
        this.actionsInterface = actionsInterface;
        this.lineItems = lineItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LineItemViewHolder(context, actionsInterface, LayoutInflater.from(context).inflate(R.layout.line_items_list_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LineItem lineItem = lineItems.get(position);
        ((LineItemViewHolder) holder).setup(lineItem);
    }

    @Override
    public int getItemCount() {
        return lineItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}

