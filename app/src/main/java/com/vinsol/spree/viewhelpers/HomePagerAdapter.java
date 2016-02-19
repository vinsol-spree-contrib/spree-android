package com.vinsol.spree.viewhelpers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.vinsol.spree.R;
import com.vinsol.spree.controllers.fragments.CartFragment;
import com.vinsol.spree.controllers.fragments.DashboardFragment;
import com.vinsol.spree.controllers.fragments.MoreOptionsFragment;
import com.vinsol.spree.controllers.fragments.NotificationsFragment;
import com.vinsol.spree.controllers.fragments.ProfileFragment;

/**
 * Created by vaibhav on 11/3/15.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {
    private Context context;
    public CartFragment cartFragment;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch (position) {
            case 0 : fragment = new DashboardFragment();
                break;

            case 1 : fragment = CartFragment.newInstance(false);
                this.cartFragment = (CartFragment) fragment;
                break;

            case 2 : fragment = ProfileFragment.newInstance(false);
                break;

            case 3 : fragment = NotificationsFragment.newInstance();
                break;

            case 4 : fragment = MoreOptionsFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(context).inflate(R.layout.home_pager_tab, null);
        ImageView img = (ImageView) v.findViewById(R.id.home_pager_tab_icon);

        switch (position) {
            case 0:
                img.setImageResource(R.drawable.home_states);
                break;
            case 1:
                img.setImageResource(R.drawable.cart_states);
                break;
            case 2:
                img.setImageResource(R.drawable.profile_states);
                break;
            case 3:
                img.setImageResource(R.drawable.notifications_states);
                break;
        }

        return v;
    }
}
