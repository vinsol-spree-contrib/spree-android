package com.vinsol.spree.viewhelpers;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vinsol.spree.R;
import com.vinsol.spree.utils.Log;

public class ProductDetailColorOptionHelper extends ProductDetailOptionHelper {


    public ProductDetailColorOptionHelper(Context context, LayoutInflater layoutInflater, LinearLayout parent) {
        super(context, layoutInflater, parent);

        selectedItemBG      = ContextCompat.getDrawable(context, R.drawable.color_option_selected);
        nonSelectedItemBG   = ContextCompat.getDrawable(context, R.drawable.transparent);
    }

    public void addOptionValues(LinearLayout optionValuesLL, final String optionValueString) {
        final View option = layoutInflater.inflate(R.layout.products_detail_color_option_item, optionValuesLL, false);

        int color = android.R.color.white;
        try {
            color = Color.parseColor(optionValueString);
        } catch(Exception e) {
            Log.d("Issue in parsing color " + e);
        }

        View colorView = option.findViewById(R.id.products_detail_color_option_value);

        colorView.setBackgroundColor(color);

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isThisOptionEnabled) {
                    selectedValue = optionValueString;
                    unSelectOptions();
                    option.setBackground(selectedItemBG);

                    if (onOptionItemSelectedListener != null) {
                        onOptionItemSelectedListener.onOptionItemSelected(selectedValue);
                    }
                } else {
                    Toast.makeText(context, messageForDisabledState, Toast.LENGTH_LONG).show();
                }
            }
        });

        optionValuesLL.addView(option);
    }
}
