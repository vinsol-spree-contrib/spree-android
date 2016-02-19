package com.vinsol.spree.utils;

import com.vinsol.spree.SpreeApplication;

/**
 * Created by vaibhav on 10/28/15.
 */
public class DisplayArea {
    private static int displayWidth;

    private static int drawerPageRowBlockItemCount;
    private static int drawerPageBlockItemWidth;


    public static int getDisplayWidth() {
        return displayWidth;
    }

    public static void setDisplayWidth(int displayWidth) {
        DisplayArea.displayWidth = displayWidth;
        calculateBlockItemAttribs();
    }

    public static int getDrawerPageRowBlockItemCount() {
        return drawerPageRowBlockItemCount;
    }

    public static int getDrawerPageBlockItemWidth() {
        return drawerPageBlockItemWidth;
    }

    private static void calculateBlockItemAttribs() {
        if(Common.convertPixelToDp(SpreeApplication.getContext(), displayWidth) >= 360) {
            drawerPageRowBlockItemCount = 4;
        } else {
            drawerPageRowBlockItemCount = 3;
        }
        drawerPageBlockItemWidth = displayWidth / drawerPageRowBlockItemCount;
    }
}
