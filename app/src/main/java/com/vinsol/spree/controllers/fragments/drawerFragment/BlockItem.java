package com.vinsol.spree.controllers.fragments.drawerFragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vinsol.spree.R;
import com.vinsol.spree.models.Taxon;
import com.vinsol.spree.utils.Blender;

/**
 * Created by Infernus on 23/12/15.
 */
public class BlockItem extends RelativeLayout {

    private ImageView icon;
    private TextView label;

    public BlockItem(Context context, Taxon taxon, int width) {
        super(context);
        init();
        initUI(width);
        setupForTaxon(taxon);
    }

    private void init() {
        inflate(getContext(), R.layout.drawer_page_block_item, this);
    }

    private void initUI(int width) {
        icon = (ImageView) findViewById(R.id.drawer_page_block_item_image);
        label = (TextView) findViewById(R.id.drawer_page_block_item_title);

        setLayoutParams(new LayoutParams(width, (int) getContext().getResources().getDimension(R.dimen.drawer_page_block_item_height)));
    }

    private void setupForTaxon(Taxon taxon) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                icon.setImageBitmap(Blender.blendDrawable(bitmap, ContextCompat.getColor(getContext(), R.color.googleYellow)));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };
        icon.setTag(target);

        Picasso.with(icon.getContext())
                .load(taxon.getIconUrl())
                .into(target);

        label.setText(taxon.getName());
    }
}
