package com.vinsol.spree.viewhelpers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vinsol.spree.R;
import com.vinsol.spree.models.AvailableOption;

public class ProductDetailOptionHelper {

    protected Context context;

    protected LayoutInflater layoutInflater;
    protected LinearLayout parent;

    protected LinearLayout optionsLayout;

    protected String type;
    protected String selectedValue;

    private LinearLayout optionValuesLL;

    protected Drawable selectedItemBG;
    protected Drawable nonSelectedItemBG;

    protected boolean isThisOptionEnabled = false;
    protected String messageForDisabledState = "";

    protected int position;

    protected OnOptionItemSelectedListener onOptionItemSelectedListener;

    public ProductDetailOptionHelper(Context context, LayoutInflater layoutInflater, LinearLayout parent) {
        this.context = context;
        this.layoutInflater = layoutInflater;
        this.parent = parent;

        selectedItemBG      = context.getResources().getDrawable(R.drawable.option_selected);
        nonSelectedItemBG   = context.getResources().getDrawable(R.drawable.option_normal);
    }

    public void showOptions(AvailableOption availableOption) {
        this.type = availableOption.getType();
        View optionLayout = layoutInflater.inflate(R.layout.products_detail_options_item, parent, false);

        // option layout header view
        TextView optionHeader = (TextView) optionLayout.findViewById(R.id.products_detail_options_header);
        optionHeader.setText(availableOption.getType());

        // option values
        optionValuesLL = (LinearLayout) optionLayout.findViewById(R.id.products_detail_options_values_linear_layout);

        // add option values to linear layout
        for(String availableOptionValue : availableOption.getValues()) {
            addOptionValues(optionValuesLL, availableOptionValue);
        }

        parent.addView(optionLayout);
    }

    public void updateOptions(AvailableOption availableOption) {
        // remove selected value
        setSelectedValue(null);

        // option values
        optionValuesLL.removeAllViews();

        // add option values to linear layout
        for(String availableOptionValue : availableOption.getValues()) {
            addOptionValues(optionValuesLL, availableOptionValue);
        }
    }



    public void setThisOptionEnabled(boolean thisOptionEnabled) {
        this.isThisOptionEnabled = thisOptionEnabled;
    }

    public void setMessageForDisabledState(String messageForDisabledState) {
        this.messageForDisabledState = messageForDisabledState;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    protected void addOptionValues(LinearLayout optionValuesLL, final String optionValueString) {
        final View option = layoutInflater.inflate(R.layout.products_detail_option_item, optionValuesLL, false);

        TextView optionValue = (TextView) option.findViewById(R.id.products_detail_option_value_text);
        optionValue.setText(optionValueString);

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isThisOptionEnabled) {
                    setSelectedValue(optionValueString);
                    unSelectOptions();
                    option.setBackground(selectedItemBG);

                    if(onOptionItemSelectedListener != null) {
                        onOptionItemSelectedListener.onOptionItemSelected(selectedValue);
                    }
                } else {
                    Toast.makeText(context, messageForDisabledState, Toast.LENGTH_LONG).show();
                }
            }
        });

        optionValuesLL.addView(option);
    }

    protected void unSelectOptions() {
        for(int i=0; i < optionValuesLL.getChildCount(); i++) {
            optionValuesLL.getChildAt(i).setBackground(nonSelectedItemBG);
        }
    }

    public void setOnOptionItemSelectedListener(OnOptionItemSelectedListener onOptionItemSelectedListener) {
        this.onOptionItemSelectedListener = onOptionItemSelectedListener;
    }

    public interface OnOptionItemSelectedListener {
        public void onOptionItemSelected(String selectedItem);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }
}
