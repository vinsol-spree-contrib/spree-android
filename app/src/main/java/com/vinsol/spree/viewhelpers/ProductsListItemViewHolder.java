package com.vinsol.spree.viewhelpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vinsol.spree.R;
import com.vinsol.spree.SpreeApplication;
import com.vinsol.spree.events.OpenProductDetailEvent;
import com.vinsol.spree.models.Product;
import com.vinsol.spree.utils.BusProvider;

/**
 * Created by vaibhav on 11/18/15.
 */
public class ProductsListItemViewHolder extends RecyclerView.ViewHolder {
    private ImageView img, wishlist;
    private TextView name, price;
    private View ratingStars;
    private RelativeLayout descContainer;

    public ProductsListItemViewHolder(View itemView) {
        super(itemView);
        img        = (ImageView) itemView.findViewById(R.id.products_list_view_item_img);
        wishlist   = (ImageView) itemView.findViewById(R.id.products_list_view_item_wishlist_img);
        name       = (TextView)  itemView.findViewById(R.id.products_list_view_item_name);
        ratingStars=             itemView.findViewById(R.id.products_list_view_item_rating_container);
        price      = (TextView)  itemView.findViewById(R.id.products_list_view_item_price);
        descContainer = (RelativeLayout) itemView.findViewById(R.id.products_list_view_item_txt_container);
    }

    public void setup(Context context, Product product) {
        setClickListeners(product.getId());
        name.setText(product.getName());
        // rating
        new RatingStarsHelper(context, ratingStars).setRating(product.getAverageRating());
        price.setText(product.getDisplayPrice());
        if (product.getImages()!=null && !product.getImages().isEmpty()) {
            Picasso.with(SpreeApplication.getContext()).load(product.getImages().get(0).getProductUrl()).into(img);
        } else {
            img.setImageResource(R.drawable.placeholder);
        }
    }


    private void setClickListeners(final int productId) {
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO : add or remove product from wishlist v2
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductDetail(productId);
            }
        });
        descContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductDetail(productId);
            }
        });
    }

    private void openProductDetail(int productId) {
        BusProvider.getInstance().post(new OpenProductDetailEvent(productId));
    }
}
