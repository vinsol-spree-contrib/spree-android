package com.vinsol.spree.views;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.vinsol.spree.R;
import com.vinsol.spree.api.models.NameValuePair;
import com.vinsol.spree.models.Filter;
import com.vinsol.spree.utils.Common;
import com.vinsol.spree.utils.Log;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 12/15/15.
 */
public class MultiSelectFilterLayout extends BaseFilterLayout {
    private ArrayList<String> selectedValues = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxes = new ArrayList<>();

    public MultiSelectFilterLayout(Context context, Filter filter, LinearLayout parent) {
        super(context, filter, parent);
        init();
    }

    @Override
    protected void init() {
        super.init();
        LinearLayout row = null;
        for (int i = 0; i < filter.getValues().size(); i++) {
            String value = filter.getValues().get(i);
            if (i%2==0) {
                row = createNewRow();
                createView(row, value, false);
                optionsLayout.addView(row);
            }
            else {
                createView(row, value, false);
            }
        }
    }

    private void createView(LinearLayout parent, final String value, boolean isChecked) {
        LinearLayout item = (LinearLayout) inflate(context, R.layout.filter_multi_select, null);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight=1;
        item.setLayoutParams(layoutParams);
        CheckBox checkBox = (CheckBox) item.findViewById(R.id.filter_multi_select_checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) selectedValues.add(value);
                else selectedValues.remove(value);
            }
        });
        checkBox.setChecked(isChecked);
        checkBoxes.add(checkBox);
        TextView textView = (TextView) item.findViewById(R.id.filter_multi_select_checkbox_txt);
        textView.setText(value);
        if (filter.getName().equals("Color")) {
            textView.setText("");
            LayoutParams textLayoutParams = (LayoutParams)textView.getLayoutParams();
            textLayoutParams.height = Common.convertDpToPixel(context, 30);
            textLayoutParams.width = Common.convertDpToPixel(context, 30);
            int color = android.R.color.white;
            try {
                color = Color.parseColor(value);
            } catch(Exception e) {
                Log.d("Issue in parsing color " + e);
            }
            textView.setBackgroundColor(color);
        }
        parent.addView(item);
    }

    //TODO : to be used when retrofit 2.1 is out
    /*@Override
    public List<NameValuePair> getQueryString() {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        String searchKey = "q[" + filter.getSearchKey() + "][]";

        for (String selectedValue :
                selectedValues) {
            NameValuePair nameValuePair = new NameValuePair(searchKey, selectedValue);
            nameValuePairs.add(nameValuePair);
        }
        return nameValuePairs;
    }*/

    @Override
    public String getQueryString() {
        String query = "";
        for (String selectedValue :
                selectedValues) {
            query += "q[" + filter.getSearchKey() + "][]=" + selectedValue + "&";
        }
        return query;
    }

    @Override
    public void clear() {
        for (CheckBox cb :
                checkBoxes) {
            cb.setChecked(false);
        }
    }

    @Override
    public void setSelectedFilter(ArrayList<String> values) {
        for (int i = 0; i < filter.getValues().size(); i++) {
            String val = filter.getValues().get(i);
            if (values.contains(val)) checkBoxes.get(i).setChecked(true);
        }
    }

    @Override
    public Filter getSelectedFilter() {
        if (!selectedValues.isEmpty()) {
            Filter selectedFilter = filter;
            selectedFilter.setValues(selectedValues);
            return selectedFilter;
        }
        return null;
    }
}
