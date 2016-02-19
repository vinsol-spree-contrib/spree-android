package com.vinsol.spree.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.vinsol.spree.SpreeApplication;
import com.vinsol.spree.cache.Cache;

/**
 * Created by vaibhav on 10/20/15.
 */
public class SharedPreferencesHelper {
    private static final String PREFS_TOTAL_ITEMS = "prefs_total_items";
    private static final String PREFS_CACHE = "prefs_cache";
    private static final String DEFAULT_PREFERENCES = "spree";

    public static SharedPreferences getSharedPreferences() {
        return SpreeApplication.getContext().getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE);
    }

    // Cache -------------------------------------
    public static boolean isCacheAvailable() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String json = sharedPreferences.getString(PREFS_CACHE, "");
        if(json.equals("")) {
            return false;
        } else {
            return true;
        }
    }

    public static Cache getCache() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        String json = sharedPreferences.getString(PREFS_CACHE, "");
        if(json.equals("")) {
            return null;
        } else {
            return Cache.deserialize(json);
        }
    }

    public static void saveCache(Cache cache) {
        saveString(PREFS_CACHE, Cache.serialize(cache));
    }
    // ------------------------------------------------------------

    public static int getTotalItems() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getInt(PREFS_TOTAL_ITEMS, 0);
    }

    public static void saveTotalItems(int totalItems) {
        saveInteger(PREFS_TOTAL_ITEMS, totalItems);
    }
    // -----------------------------------------------------

    private static void saveString(String prefKey, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putString(prefKey, value);
        prefEditor.commit();
    }

    private static void saveBoolean(String prefKey, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putBoolean(prefKey, value);
        prefEditor.commit();
    }

    private static void saveInteger(String prefKey, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putInt(prefKey, value);
        prefEditor.commit();
    }
}
