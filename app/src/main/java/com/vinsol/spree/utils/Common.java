package com.vinsol.spree.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import com.vinsol.spree.api.models.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vaibhav on 10/28/15.
 */
public class Common {
    public static int convertDpToPixel(Context context, float dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    public static int convertPixelToDp(Context context, float px){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return (int) dp;
    }

    public static int calculateStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    //TODO : to be used when retrofit 2.1 is out
    public static HashMap<String, String> getPathMap(List<NameValuePair> params) {
        HashMap<String, String> paramMap = new HashMap<>();
        String paramValue;

        for (NameValuePair paramPair : params) {
            if (!TextUtils.isEmpty(paramPair.getName())) {
                try {
                    Log.d("Map > param name " + paramPair.getName());
                    if (paramMap.containsKey(paramPair.getName())) {
                        Log.d("Map > already contains this");
                        // Add the duplicate key and new value onto the previous value
                        // so (key, value) will now look like (key, value&key=value2)
                        // which is a hack to work with Retrofit's QueryMap
                        paramValue = paramMap.get(paramPair.getName());
                        paramValue += "&" + paramPair.getName() + "=" + URLEncoder.encode(String.valueOf(paramPair.getValue()), "UTF-8");
//                        paramValue += "&" + paramPair.getName() + "=" + String.valueOf(paramPair.getValue());
                        Log.d("Map > param value " + paramValue);
                    } else {
                        Log.d("Map > does not already contains this");
                        // This is the first value, so directly map it
//                        paramValue = String.valueOf(paramPair.getValue());
                        paramValue = URLEncoder.encode(String.valueOf(paramPair.getValue()), "UTF-8");
                        Log.d("Map > param value " + paramValue);
                    }
                    paramMap.put(paramPair.getName(), paramValue);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return paramMap;
    }

    public static void openBrowserWithURL(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url)).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
        context.startActivity(intent);
    }
}
