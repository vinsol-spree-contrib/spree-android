package com.vinsol.spree.utils;

/**
 * Created by vaibhav on 10/30/15.
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class Blender {

    public static Bitmap blendDrawable(Bitmap bitmap, int color) {

        Bitmap resourceBlendedBitmap = Blender.getBlendedBitmap(bitmap, color);
        return resourceBlendedBitmap;
    }

    public static Bitmap getBlendedBitmap(Bitmap bitmap, int color) {

        Bitmap greyScaleBitmap = bitmap;

        Bitmap colorBitmap = Bitmap.createBitmap(greyScaleBitmap.getWidth(), greyScaleBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        colorBitmap.eraseColor(color);

        Bitmap blendedBitmap = Bitmap.createBitmap(greyScaleBitmap.getWidth(), greyScaleBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(blendedBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
        c.drawBitmap(greyScaleBitmap, 0, 0 , null);
        c.drawBitmap(colorBitmap, 0, 0, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        c.drawBitmap(greyScaleBitmap, 0, 0 , paint);

        paint.setXfermode(null);
        return blendedBitmap;
    }
}
