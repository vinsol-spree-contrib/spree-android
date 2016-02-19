package com.vinsol.spree.controllers.fragments.drawerFragment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.vinsol.spree.R;
import com.vinsol.spree.models.Taxon;

import java.util.ArrayList;

/**
 * Created by Infernus on 23/12/15.
 */
public class DrawerPageAdapter extends ArrayAdapter<Taxon> {
    private static final int VIEW_TYPE_LINEAR = 0;
    private static final int VIEW_TYPE_BLOCK = 1;
    private static final int VIEW_TYPES_COUNT = 2;

    private Activity activity;
    private DrawerActionListener drawerActionListener;
    private int expandedTaxonId = -1;

    public DrawerPageAdapter(Activity activity, ArrayList<Taxon> taxons, DrawerActionListener drawerActionListener) {
        super(activity, R.layout.drawer_page_row_linear, taxons);
        this.activity = activity;
        this.drawerActionListener = drawerActionListener;
    }

    public int getExpandedTaxonId() {
        return expandedTaxonId;
    }

    public void setExpandedTaxonId(int expandedTaxonId) {
        this.expandedTaxonId = expandedTaxonId;
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position).getChildren().size() == 0) {
            return VIEW_TYPE_LINEAR;
        } else {
            return VIEW_TYPE_BLOCK;
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPES_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(getItemViewType(position) == VIEW_TYPE_LINEAR) {
            DrawerPageItemRowLinearHolder holder;
            if(convertView == null) {
                LayoutInflater inflater = activity.getLayoutInflater();
                convertView = inflater.inflate(R.layout.drawer_page_row_linear, parent, false);
                holder = new DrawerPageItemRowLinearHolder(convertView, drawerActionListener);

                convertView.setTag(holder);
            } else {
                holder = (DrawerPageItemRowLinearHolder) convertView.getTag();
            }

            holder.setupForTaxon(getItem(position));
            return convertView;
        } else {
            DrawerPageItemRowBlockHolder holder;
            if(convertView == null) {
                LayoutInflater inflater = activity.getLayoutInflater();
                convertView = inflater.inflate(R.layout.drawer_page_row_block, parent, false);
                holder = new DrawerPageItemRowBlockHolder(convertView, drawerActionListener, this);

                convertView.setTag(holder);
            } else {
                holder = (DrawerPageItemRowBlockHolder) convertView.getTag();
            }

            holder.setupForTaxon(getItem(position));
            return convertView;
        }
    }
}
