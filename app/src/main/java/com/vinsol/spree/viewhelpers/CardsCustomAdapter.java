package com.vinsol.spree.viewhelpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.vinsol.spree.models.Card;

import java.util.List;

/**
 * Created by vaibhav on 12/24/15.
 */
public class CardsCustomAdapter extends ArrayAdapter<Card> {
    private Context context;
    private List<Card> addresses;
    private int resource;

    public CardsCustomAdapter(Context context, int resource, List<Card> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.addresses = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CardsListItemViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            holder = new CardsListItemViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (CardsListItemViewHolder) convertView.getTag();
        }

        holder.setup(getItem(position));

        return convertView;
    }
}
