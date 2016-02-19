package com.vinsol.spree.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vinsol.spree.R;
import com.vinsol.spree.models.Filter;

import java.util.ArrayList;

/**
 * Created by vaibhav on 12/14/15.
 */
public abstract class BaseFilterLayout extends LinearLayout {
    protected Context context;
    protected Filter filter;
    protected LinearLayout optionsLayout;
    private ImageView expandCollapseImg;
    private TextView name;
    private RelativeLayout header;
    private LinearLayout parent;
    private boolean isExpanded = true;

    public BaseFilterLayout(Context context, Filter filter, LinearLayout parent) {
        super(context);
        this.context = context;
        this.filter = filter;
        this.parent = parent;
    }

    protected void init() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.filter_layout, this);

        header              = (RelativeLayout) view.findViewById(R.id.filter_header_container);
        expandCollapseImg   = (ImageView) view.findViewById(R.id.filter_header_expand_collapse_img);
        name                = (TextView) view.findViewById(R.id.filter_header_name_txt);
        optionsLayout       = (LinearLayout) view.findViewById(R.id.filter_layout_body_container);
        name.setText(filter.getName());
        setListeners();
        parent.addView(view);
    }

    private void setListeners() {
        header.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) collapse();
                else expand();
            }
        });
    }

    private void collapse() {
        final int initialHeight = optionsLayout.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    optionsLayout.setVisibility(View.GONE);
                    expandCollapseImg.setRotation(0);
                    isExpanded = false;
                } else {
                    optionsLayout.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    optionsLayout.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        animation.setDuration((int)(initialHeight / optionsLayout.getContext().getResources().getDisplayMetrics().density));
        optionsLayout.startAnimation(animation);
    }

    private void expand() {
        optionsLayout.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = optionsLayout.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        optionsLayout.getLayoutParams().height = 1;
        optionsLayout.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                optionsLayout.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                optionsLayout.requestLayout();
                if (interpolatedTime==1) {
                    expandCollapseImg.setRotation(180);
                    isExpanded = true;
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / optionsLayout.getContext().getResources().getDisplayMetrics().density));
        optionsLayout.startAnimation(a);
    }

    protected LinearLayout createNewRow() {
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.margin_10dp);
        linearLayout.setOrientation(HORIZONTAL);
        linearLayout.setWeightSum(2);
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    abstract public String getQueryString();
    abstract public void clear();
    abstract public void setSelectedFilter(ArrayList<String> values);
    abstract public Filter getSelectedFilter();
}
