package com.vinsol.spree.utils;

import static com.vinsol.spree.utils.Constants.DEBUG;
/**
 * Created by vaibhav on 9/30/15.
 */
public class Log {
    private static String app = "Spree";

    public static final void d(Throwable throwable) {
        if (DEBUG)
            android.util.Log.d(app, "", throwable);
    }

    public static final void d(Object object) {
        if (DEBUG)
            android.util.Log.d(app, object != null ? "*** " + object.toString() : null);
    }

    public static final void d(Object object, Throwable throwable) {
        if (DEBUG)
            android.util.Log.d(app, object != null ? "*** " + object.toString() : null, throwable);
    }
}
