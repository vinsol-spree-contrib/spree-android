package com.vinsol.spree.controllers.fragments.drawerFragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vinsol.spree.R;
import com.vinsol.spree.SpreeApplication;
import com.vinsol.spree.models.Taxon;
import com.vinsol.spree.utils.DisplayArea;

/**
 * Created by Infernus on 23/12/15.
 */
public class DrawerPageItemRowBlockHolder {
    private static final int BLOCK_ITEM_ID_BASE = 123;
    private static final int EXPAND_COLLAPSE_DURATION = 200;

    private ImageView chevron;
    private ImageView handleChevron;
    private TextView titleLabel;

    private RelativeLayout itemsContainer;
    private RelativeLayout expansionHandle;

    private int blockRowHeight;
    private int blockRowsCount;

    private boolean isExpanded = false;

    private Taxon taxon;

    DrawerActionListener drawerActionListener;
    DrawerPageAdapter adapter;

    public DrawerPageItemRowBlockHolder(View view, DrawerActionListener drawerActionListener, DrawerPageAdapter adapter) {
        this.drawerActionListener = drawerActionListener;
        this.adapter = adapter;

        chevron = (ImageView) view.findViewById(R.id.drawer_page_row_block_chevron);
        handleChevron = (ImageView) view.findViewById(R.id.drawer_page_row_block_handle_chevron);
        titleLabel = (TextView) view.findViewById(R.id.drawer_page_row_block_title);
        itemsContainer = (RelativeLayout) view.findViewById(R.id.drawer_page_row_block_items_container);
        expansionHandle = (RelativeLayout) view.findViewById(R.id.drawer_page_row_block_expansion_handle);

        blockRowHeight = (int) SpreeApplication.getContext().getResources().getDimension(R.dimen.drawer_page_block_item_height);
    }

    public void setupForTaxon(final Taxon taxon) {
        this.taxon = taxon;

        titleLabel.setText(taxon.getName());

        blockRowsCount = taxon.getChildren().size() / DisplayArea.getDrawerPageRowBlockItemCount();
        if(taxon.getChildren().size() % DisplayArea.getDrawerPageRowBlockItemCount() > 0) blockRowsCount++;

        if(blockRowsCount == 1) {
            expansionHandle.setVisibility(View.GONE);
            chevron.setVisibility(View.GONE);
        } else {
            expansionHandle.setVisibility(View.VISIBLE);
            chevron.setVisibility(View.GONE);

            expansionHandle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleState();
                }
            });

            chevron.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleState();
                }
            });
        }

        for(int i = 0; i < taxon.getChildren().size(); i++) {
            final Taxon blockTaxon = taxon.getChildren().get(i);
            BlockItem blockItem = new BlockItem(SpreeApplication.getContext(), blockTaxon, DisplayArea.getDrawerPageBlockItemWidth());
            blockItem.setId(BLOCK_ITEM_ID_BASE + i);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) blockItem.getLayoutParams();

            if(i < DisplayArea.getDrawerPageRowBlockItemCount()) {
                if(i > 0) params.addRule(RelativeLayout.RIGHT_OF, BLOCK_ITEM_ID_BASE + i - 1);
            } else {
                params.addRule(RelativeLayout.BELOW, BLOCK_ITEM_ID_BASE + i - DisplayArea.getDrawerPageRowBlockItemCount());
                params.addRule(RelativeLayout.ALIGN_LEFT, BLOCK_ITEM_ID_BASE + i - DisplayArea.getDrawerPageRowBlockItemCount());
            }

            itemsContainer.addView(blockItem);

            blockItem.setClickable(true);
            blockItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerActionListener.onTaxonSelected(blockTaxon);
                }
            });
        }

//        boolean shouldBeExpanded = adapter.getExpandedTaxonId() == taxon.getId();

        if(isExpanded) expandInstantly();
        else collapseInstantly();
    }

    public void toggleState() {
        if(isExpanded) {
            adapter.setExpandedTaxonId(-1);
            collapse();
        } else {
            adapter.setExpandedTaxonId(taxon.getId());
            expand();
        }
    }

    private void expand() {
        expand(EXPAND_COLLAPSE_DURATION);
    }

    private void expandInstantly() {
        expand(0);
    }

    private void expand(int duration) {
        isExpanded = true;
        animateHeight(blockRowHeight * blockRowsCount, duration);

        ObjectAnimator chevronRotateAnimation = ObjectAnimator.ofFloat(chevron, View.ROTATION, 180);
        chevronRotateAnimation.setDuration(duration);
        chevronRotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        chevronRotateAnimation.start();

        ObjectAnimator chevron2RotateAnimation = ObjectAnimator.ofFloat(handleChevron, View.ROTATION, 180);
        chevron2RotateAnimation.setDuration(duration);
        chevron2RotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        chevron2RotateAnimation.start();
    }

    private void collapse() {
        collapse(EXPAND_COLLAPSE_DURATION);
    }

    private void collapseInstantly() {
        collapse(0);
    }

    private void collapse(int duration) {
        isExpanded = false;
        animateHeight(blockRowHeight, duration);

        ObjectAnimator chevronRotateAnimation = ObjectAnimator.ofFloat(chevron, View.ROTATION, 0);
        chevronRotateAnimation.setDuration(duration);
        chevronRotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        chevronRotateAnimation.start();

        ObjectAnimator chevron2RotateAnimation = ObjectAnimator.ofFloat(handleChevron, View.ROTATION, 0);
        chevron2RotateAnimation.setDuration(duration);
        chevron2RotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        chevron2RotateAnimation.start();
    }

    private void animateHeight(int toHeight, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(itemsContainer.getHeight(), toHeight);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                itemsContainer.getLayoutParams().height = ((Float) animator.getAnimatedValue()).intValue();
                itemsContainer.requestLayout();
            }
        });

        valueAnimator.start();
    }
}
