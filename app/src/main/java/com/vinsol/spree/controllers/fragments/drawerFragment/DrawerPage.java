package com.vinsol.spree.controllers.fragments.drawerFragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.vinsol.spree.R;
import com.vinsol.spree.models.Taxon;
import com.vinsol.spree.utils.DisplayArea;

import java.util.ArrayList;

/**
 * Created by Infernus on 23/12/15.
 */
public class DrawerPage extends RelativeLayout {
    private static final int ANIMATION_DURATION = 350;

    private int level = 1;
    private Activity activity;
    private DrawerActionListener drawerActionListener;

    private ListView listView;
    private View fader;

    public DrawerPage(Context context) {
        super(context);
        init();
    }

    public DrawerPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        extractAttributes(attrs);
        init();
    }

    public DrawerPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        extractAttributes(attrs);
        init();
    }

    protected void setup(Activity activity, ArrayList<Taxon> taxons, DrawerActionListener drawerActionListener) {
        this.activity = activity;
        this.drawerActionListener = drawerActionListener;

        listView.setAdapter(new DrawerPageAdapter(activity, taxons, drawerActionListener));
    }

    protected void show() {
        show(ANIMATION_DURATION);
    }

    protected void showInstantly() {
        show(0);
    }

    protected void hide() {
        hide(ANIMATION_DURATION);
    }

    protected void hideInstantly() {
        hide(0);
    }

    protected void scrollToTop() {
        // Using try-catch to save from crashing if list has no items
        try {
            listView.setSelection(0);
        } catch (Exception e) {}
    }








    //////////////////////////////////////////
    // Private Methods
    //////////////////////////////////////////

    private void extractAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DrawerPage, 0, 0);
        level = a.getInt(R.styleable.DrawerPage_level, 1);
        a.recycle();

        if(level < 1 || level > 2) {
            throw new RuntimeException("Invalid value of level. It can only be 1 or 2");
        }
    }

    private void init() {
        inflate(getContext(), R.layout.drawer_page, this);
        initUI();

        // hide the page initially if it is second level page
        if(level == 2) {
            hideInstantly();
        }
    }

    private void initUI() {
        listView = (ListView) findViewById(R.id.drawer_page_list);
        fader = findViewById(R.id.drawer_page_fader);
    }

    private void show(int duration) {
        fader.setAlpha(1f);
        fader.setVisibility(View.VISIBLE);

        ObjectAnimator pageSlideAnim = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 0f);
        pageSlideAnim.setDuration(duration);
        pageSlideAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator faderAlphaAnim = ObjectAnimator.ofFloat(fader, View.ALPHA, 0);
        faderAlphaAnim.setDuration(duration);
        faderAlphaAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.playTogether(pageSlideAnim, faderAlphaAnim);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                fader.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        set.start();
    }

    private void hide(int duration) {
        fader.setAlpha(0f);
        fader.setVisibility(View.VISIBLE);

        ObjectAnimator pageSlideAnim = ObjectAnimator.ofFloat(this, View.TRANSLATION_X, level == 1 ? -DisplayArea.getDisplayWidth() : DisplayArea.getDisplayWidth());
        pageSlideAnim.setDuration(duration);
        pageSlideAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator faderAlphaAnim = ObjectAnimator.ofFloat(fader, View.ALPHA, 1f);
        faderAlphaAnim.setDuration(duration);
        faderAlphaAnim.setInterpolator(new AccelerateDecelerateInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.playTogether(pageSlideAnim, faderAlphaAnim);
        set.start();
    }
}
