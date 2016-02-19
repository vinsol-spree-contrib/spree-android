package com.vinsol.spree.controllers.fragments.drawerFragment;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vinsol.spree.R;
import com.vinsol.spree.models.Taxon;
import com.vinsol.spree.utils.Blender;

/**
 * Created by Infernus on 23/12/15.
 */
public class DrawerPageItemRowLinearHolder {
    private View view;
    private ImageView icon;
    private TextView label;
    private DrawerActionListener drawerActionListener;

    public DrawerPageItemRowLinearHolder(View view, DrawerActionListener drawerActionListener) {
        this.view = view;
        this.drawerActionListener = drawerActionListener;
        view.setClickable(true);
        icon = (ImageView) view.findViewById(R.id.drawer_page_row_linear_image);
        label = (TextView) view.findViewById(R.id.drawer_page_row_linear_title);
    }

    public void setupForTaxon(final Taxon taxon) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                icon.setImageBitmap(Blender.blendDrawable(bitmap, ContextCompat.getColor(icon.getContext(), R.color.googleYellow)));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {}

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {}
        };
        icon.setTag(target);
        Picasso.with(icon.getContext()).load(taxon.getIconUrl()).into(target);

        label.setText(taxon.getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerActionListener.onTaxonSelected(taxon);
            }
        });
    }
}
