package com.vinsol.spree.controllers;

import android.util.DisplayMetrics;

import com.vinsol.spree.controllers.fragments.BaseFragment;

/**
 * Created by vaibhav on 10/13/15.
 */
public interface HostActivityInterface {
    public void setSelectedFragment(BaseFragment fragment);
    public void popBackStack();
    public void addFragment(BaseFragment fragment, boolean addToBackstack, boolean withAnimation);
    public DisplayMetrics getDisplayMetrics();
}
