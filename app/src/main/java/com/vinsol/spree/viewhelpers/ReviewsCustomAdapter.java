package com.vinsol.spree.viewhelpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.vinsol.spree.models.Review;

import java.util.List;

public class ReviewsCustomAdapter extends ArrayAdapter<Review> {
    private Context context;
    private int resource;

    public ReviewsCustomAdapter(Context context, int resource, List<Review> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ReviewListItemViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            holder = new ReviewListItemViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ReviewListItemViewHolder) convertView.getTag();
        }

        holder.setup(getItem(position));

        return convertView;
    }
}