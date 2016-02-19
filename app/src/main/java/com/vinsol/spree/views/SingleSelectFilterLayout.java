package com.vinsol.spree.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vinsol.spree.R;
import com.vinsol.spree.api.models.NameValuePair;
import com.vinsol.spree.models.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vaibhav on 12/15/15.
 */
public class SingleSelectFilterLayout extends BaseFilterLayout {
    private ArrayList<RadioButton> radioButtons = new ArrayList<>();
    private String selectedValue;

    public SingleSelectFilterLayout(Context context, Filter filter, LinearLayout parent) {
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
        LinearLayout item = (LinearLayout) inflate(context, R.layout.filter_single_select, null);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight=1;
        item.setLayoutParams(layoutParams);
        final RadioButton radio = (RadioButton) item.findViewById(R.id.filter_single_select_radio_btn);
        radio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectedValue = value;
                    deselectOtherOptions(radio);
                }
            }
        });
        radio.setChecked(isChecked);
        radioButtons.add(radio);
        ((TextView) item.findViewById(R.id.filter_single_select_radio_btn_txt)).setText(value);
        parent.addView(item);
    }

    //TODO : to be used when retrofit 2.1 is out
    /*@Override
    public List<NameValuePair> getQueryString() {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        String searchKey = "q[" + filter.getSearchKey() + "][]";
        if (selectedValue!=null) {
            NameValuePair nameValuePair = new NameValuePair(searchKey, selectedValue);
            nameValuePairs.add(nameValuePair);
        }
        return nameValuePairs;
    }*/

    @Override
    public String getQueryString() {
        if (selectedValue!=null) return ("q[" + filter.getSearchKey() + "][]=" + selectedValue + "&");
        return "";
    }

    private void deselectOtherOptions(RadioButton radio) {
        for(RadioButton rb : radioButtons) {
            if(!rb.equals(radio)) rb.setChecked(false);
        }
    }

    @Override
    public void clear() {
        for (RadioButton rb :
                radioButtons) {
            rb.setChecked(false);
        }
    }

    @Override
    public void setSelectedFilter(ArrayList<String> values) {
        for (int i = 0; i < filter.getValues().size(); i++) {
            String val = filter.getValues().get(i);
            if (values.contains(val)) radioButtons.get(i).setChecked(true);
        }
    }

    @Override
    public Filter getSelectedFilter() {
        if (selectedValue!=null) {
            Filter selectedFilter = filter;
            ArrayList<String> selected = new ArrayList<>();
            selected.add(selectedValue);
            selectedFilter.setValues(selected);
            return selectedFilter;
        }
        return null;
    }
}
