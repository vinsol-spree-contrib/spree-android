package com.vinsol.spree.viewhelpers;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vinsol.spree.R;
import com.vinsol.spree.SpreeApplication;
import com.vinsol.spree.events.OpenProductDetailEvent;
import com.vinsol.spree.models.Product;
import com.vinsol.spree.utils.BusProvider;

/**
 * Created by vaibhav on 11/19/15.
 */
public class ProductsGridItemViewHolder extends RecyclerView.ViewHolder {
    private ImageView img, wishlist;
    private TextView name, price;
    private View ratingStars;
    private CardView cardView;
    private LinearLayout descContainer;

    public ProductsGridItemViewHolder(View itemView) {
        super(itemView);
        img             = (ImageView)   itemView.findViewById(R.id.products_grid_view_item_img);
        wishlist        = (ImageView)   itemView.findViewById(R.id.products_grid_view_item_wishlist_img);
        name            = (TextView)    itemView.findViewById(R.id.products_grid_view_item_name);
        price           = (TextView)    itemView.findViewById(R.id.products_grid_view_item_price);
        ratingStars     =               itemView.findViewById(R.id.products_grid_view_item_rating_container);
        cardView        = (CardView)    itemView.findViewById(R.id.products_grid_view_item_container);
        descContainer   = (LinearLayout)itemView.findViewById(R.id.products_grid_view_item_txt_container);
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

    public CardView getCardView() {
        return cardView;
    }

    public ImageView getImg() {
        return img;
    }
}
