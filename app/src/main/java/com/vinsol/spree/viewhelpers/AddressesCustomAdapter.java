package com.vinsol.spree.viewhelpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.vinsol.spree.models.Address;

import java.util.List;

/**
 * Created by vaibhav on 12/21/15.
 */
public class AddressesCustomAdapter extends ArrayAdapter<Address> {
    private Context context;
    private int resource;

    public AddressesCustomAdapter(Context context, int resource, List<Address> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AddressesListItemViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            holder = new AddressesListItemViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (AddressesListItemViewHolder) convertView.getTag();
        }

        holder.setup(getItem(position));

        return convertView;
    }
}
