package com.vinsol.spree.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vinsol.spree.R;
import com.vinsol.spree.models.Filter;
import com.vinsol.spree.utils.Log;

import java.util.ArrayList;

/**
 * Created by vaibhav on 12/15/15.
 */
public class RangeFilterLayout extends BaseFilterLayout {
    private double min, max, selectedMinValue, selectedMaxValue;
    private int priceCalloutBodyHalfWidth, priceCalloutTriangleHalfWidth, priceCalloutMinLeftMargin, priceCalloutMaxLeftMargin;
    private int priceCalloutTriangleMinLeftMargin, priceCalloutTriangleMaxLeftMargin;
    private RangeSeekBar seekBar;
    private ImageView priceCalloutTriangle;
    private LinearLayout priceCalloutBody;
    private TextView priceRangeTextView;
    private RelativeLayout.LayoutParams priceCalloutTriangleLP, priceCalloutBodyLP;
    public RangeFilterLayout(Context context, Filter filter, LinearLayout parent) {
        super(context, filter, parent);
        init();
    }

    @Override
    protected void init() {
        super.init();
        min = Double.parseDouble(filter.getValues().get(0));
        max = Double.parseDouble(filter.getValues().get(1));
        if (min>max) {
            min = min + max;
            max = min - max;
            min = min - max;
        }
        LinearLayout row = createNewRow();
        row.setOrientation(VERTICAL);
        createView(row, min, max);
        optionsLayout.addView(row);
    }

    private void createView(LinearLayout parent, final double minValue, final double maxValue) {
        View view = inflate(context, R.layout.filter_range, null);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        priceCalloutBody      = (LinearLayout) view.findViewById(R.id.filter_range_price_callout_body);
        priceRangeTextView    = (TextView)     view.findViewById(R.id.filter_range_price_callout_text_view);
		priceCalloutTriangle  = (ImageView)    view.findViewById(R.id.filter_range_price_callout_triangle);
		priceCalloutTriangleLP = (RelativeLayout.LayoutParams) priceCalloutTriangle.getLayoutParams();
		priceCalloutBodyLP = (RelativeLayout.LayoutParams) priceCalloutBody.getLayoutParams();
		
        
        seekBar = new RangeSeekBar<Double>(minValue, maxValue, context, R.drawable.seekbar_handler, R.drawable.seekbar_handler);
        selectedMinValue = minValue;
        selectedMaxValue = maxValue;
        seekBar.setNotifyWhileDragging(true);
        seekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Double>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Double minValue, Double maxValue, double averageValueScreenPosition) {
                // handle changed range values
                Log.d("User selected new range values: MIN = " + minValue + ", MAX = " + maxValue);

                selectedMinValue = minValue;
                selectedMaxValue = maxValue;

                drawPriceCallout(minValue.intValue(), maxValue.intValue(), averageValueScreenPosition);
            }


        });
        parent.addView(seekBar);
        parent.addView(view);
        // after layout has been drawn
        ViewTreeObserver vto = seekBar.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                seekBar.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                double averageValueScreenPosition = seekBar.getAverageValueScreenPoint(minValue, maxValue);

                drawPriceCallout((int) minValue, (int) maxValue, averageValueScreenPosition);
            }
        });
    }

    private void drawPriceCallout(int minValue, int maxValue, double averageValueScreenPosition) {
        String priceRangeString = "$" + minValue + " to $" + maxValue;
        priceRangeTextView.setText(priceRangeString);

        int priceCalloutTriangleLeftMargin = (int) averageValueScreenPosition - getPriceCalloutTriangleHalfWidth();

        if(priceCalloutTriangleLeftMargin < getPriceCalloutTriangleMinLeftMargin()) {
            priceCalloutTriangleLeftMargin = getPriceCalloutTriangleMinLeftMargin();
        } else if(priceCalloutTriangleLeftMargin > getPriceCalloutTriangleMaxLeftMargin()) {
            priceCalloutTriangleLeftMargin = getPriceCalloutTriangleMaxLeftMargin();
        }

        priceCalloutTriangleLP.leftMargin = priceCalloutTriangleLeftMargin;
        priceCalloutTriangle.setLayoutParams(priceCalloutTriangleLP);

        int priceCalloutBodyLeftMargin = (int) (averageValueScreenPosition - getPriceCalloutBodyHalfWidth());

        if(priceCalloutBodyLeftMargin < getPriceCalloutMinLeftMargin()) {
            priceCalloutBodyLeftMargin = getPriceCalloutMinLeftMargin();
        } else if(priceCalloutBodyLeftMargin > getPriceCalloutMaxLeftMargin()) {
            priceCalloutBodyLeftMargin = getPriceCalloutMaxLeftMargin();
        }

        priceCalloutBodyLP.leftMargin = priceCalloutBodyLeftMargin;

        priceCalloutBody.setLayoutParams(priceCalloutBodyLP);
    }

    private int getPriceCalloutBodyHalfWidth() {
        priceCalloutBodyHalfWidth = priceCalloutBody.getWidth()/2;
        return priceCalloutBodyHalfWidth;
    }

    private int getPriceCalloutTriangleHalfWidth() {
        if(priceCalloutTriangleHalfWidth == 0) {
            priceCalloutTriangleHalfWidth = priceCalloutTriangle.getWidth()/2;
        }

        return priceCalloutTriangleHalfWidth;
    }

    private int getPriceCalloutMinLeftMargin() {
        priceCalloutMinLeftMargin = 0;
        return priceCalloutMinLeftMargin;
    }

    private int getPriceCalloutMaxLeftMargin() {
        priceCalloutMaxLeftMargin = seekBar.getWidth() - (getPriceCalloutBodyHalfWidth() * 2) - getPriceCalloutMinLeftMargin();
        return priceCalloutMaxLeftMargin;
    }

    private int getPriceCalloutTriangleMinLeftMargin() {
		if(priceCalloutTriangleMinLeftMargin == 0) {
            priceCalloutTriangleMinLeftMargin = (int) getResources().getDimension(R.dimen.margin_10dp);
		}

        return priceCalloutTriangleMinLeftMargin;
    }

    private int getPriceCalloutTriangleMaxLeftMargin() {
        if(priceCalloutTriangleMaxLeftMargin == 0) {
            priceCalloutTriangleMaxLeftMargin = seekBar.getWidth() - (getPriceCalloutTriangleHalfWidth() * 2) - getPriceCalloutTriangleMinLeftMargin();
        }

        return priceCalloutTriangleMaxLeftMargin;
    }

    //TODO : to be used when retrofit 2.1 is out
    /*@Override
    public List<NameValuePair> getQueryString() {
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        String searchKey = "q[" + filter.getSearchKey() + "][]=";
        return nameValuePairs;
    }*/

    @Override
    public String getQueryString() {
        String query = "";
        if (!(selectedMaxValue==max && selectedMinValue==min)) {
            query += "q[" + filter.getSearchKey() + "][]=" + selectedMinValue + "&q[" + filter.getSearchKey() + "][]=" + selectedMaxValue + "&";
        }
        return query;
    }

    @Override
    public void clear() {
        seekBar.setSelectedMinValue(min);
        seekBar.setSelectedMaxValue(max);
    }

    @Override
    public void setSelectedFilter(ArrayList<String> values) {
        double min = Double.parseDouble(values.get(0));
        double max = Double.parseDouble(values.get(1));
        if (min>max) {
            min = min + max;
            max = min - max;
            min = min - max;
        }
        seekBar.setSelectedMinValue(min);
        seekBar.setSelectedMaxValue(max);
    }

    @Override
    public Filter getSelectedFilter() {
        if (!(selectedMaxValue==max && selectedMinValue==min)) {
            Filter selectedFilter = filter;
            ArrayList<String> selected = new ArrayList<>();
            selected.add(String.valueOf(selectedMinValue));
            selected.add(String.valueOf(selectedMaxValue));
            selectedFilter.setValues(selected);
            return selectedFilter;
        }
        return null;
    }
}

