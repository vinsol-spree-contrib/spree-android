package com.vinsol.spree.viewhelpers;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.vinsol.spree.R;
import com.vinsol.spree.enums.ViewMode;
import com.vinsol.spree.models.Product;
import com.vinsol.spree.utils.Common;
import com.vinsol.spree.utils.DisplayArea;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 11/19/15.
 */
public class ProductsCustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Product> products;
    private int viewMode;

    public ProductsCustomAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return new ProductsCardItemViewHolder(LayoutInflater.from(context).inflate(R.layout.products_card_view_item, parent, false));
        ViewMode viewMode = ViewMode.values()[viewType];
        switch (viewMode) {
            case LIST:
                // Create a new view.
                return new ProductsListItemViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.products_list_view_item, parent, false)
                );
            case GRID:
                // Create a new view.
                return new ProductsGridItemViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.products_grid_view_item, parent, false)
                );
            case CARD:
                // Create a new view.
                return new ProductsCardItemViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.products_card_view_item, parent, false)
                );
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewMode viewMode = ViewMode.values()[getItemViewType(position)];
        Product product = products.get(position);
        switch (viewMode) {
            case LIST: ((ProductsListItemViewHolder) holder).setup(context, product);
                break;

            case GRID: int margin = Common.convertDpToPixel(context, 5);
                int imageDimension = (DisplayArea.getDisplayWidth() - Common.convertDpToPixel(context, 15))/2;
                GridLayoutManager.LayoutParams params = new GridLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(margin, margin, margin, 0);
                if (position%2==0) {
                    // Left Item
                    params.rightMargin = Common.convertDpToPixel(context, 2.5f);
                }
                else {
                    // Right Item
                    params.leftMargin  = Common.convertDpToPixel(context, 2.5f);
                }
                ((ProductsGridItemViewHolder) holder).getCardView().setLayoutParams(params);
                RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageParams.width = imageDimension;
                imageParams.height = imageDimension;
                ((ProductsGridItemViewHolder) holder).getImg().setLayoutParams(imageParams);
                ((ProductsGridItemViewHolder) holder).setup(context, product);
                break;

            case CARD: ((ProductsCardItemViewHolder) holder).setup(context, product);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public int getItemViewType(int position) {
        return this.viewMode;
    }

    public void setViewMode(ViewMode viewMode) {
        this.viewMode = viewMode.ordinal();
    }

}
