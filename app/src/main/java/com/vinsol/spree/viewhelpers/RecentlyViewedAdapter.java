package com.vinsol.spree.viewhelpers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vinsol.spree.R;
import com.vinsol.spree.SpreeApplication;
import com.vinsol.spree.events.OpenProductDetailEvent;
import com.vinsol.spree.models.Product;
import com.vinsol.spree.utils.BusProvider;

import java.util.List;

/**
 * Created by vaibhav on 11/5/15.
 */
public class RecentlyViewedAdapter extends RecyclerView.Adapter<RecentlyViewedAdapter.RecentItemViewHolder> {
    private List<Product> recentlyViewedProducts;

    public RecentlyViewedAdapter() {
    }

    public RecentlyViewedAdapter(List<Product> recentlyViewedProducts) {
        this.recentlyViewedProducts = recentlyViewedProducts;
    }

    @Override
    public RecentItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_recent_product_item, parent, false);
        return new RecentItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecentItemViewHolder holder, int position) {
        holder.setup(recentlyViewedProducts.get(position));
    }

    @Override
    public int getItemCount() {
        return recentlyViewedProducts.size()<=10?recentlyViewedProducts.size():10;
    }

    static class RecentItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView txt, price;
        public RecentItemViewHolder(View itemView) {
            super(itemView);
            img   = (ImageView) itemView.findViewById(R.id.dashboard_product_item_img);
            txt   = (TextView)  itemView.findViewById(R.id.dashboard_product_item_txt);
            price = (TextView)  itemView.findViewById(R.id.dashboard_product_item_price);
        }

        public void setup(Product product) {
            setClickListeners(product.getId());
            txt.setText(product.getName());
            price.setText(product.getDisplayPrice());
            if (product.getImages()!=null && !product.getImages().isEmpty())
                Picasso.with(SpreeApplication.getContext()).load(product.getImages().get(0).getProductUrl()).into(img);
        }

        private void setClickListeners(final int productId) {
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BusProvider.getInstance().post(new OpenProductDetailEvent(productId));
                }
            });
        }
    }
}


