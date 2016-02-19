package com.vinsol.spree.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.vinsol.spree.R;
import com.vinsol.spree.utils.StaticSingletonCustomFonts;

/**
 * Created by vaibhav on 11/19/15.
 */
public class CustomFontTextView extends TextView {

    public CustomFontTextView(Context context) {
        super(context);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(!isInEditMode()) { // // for seeing the item in layout viewer in eclipse
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTypeface);
            String fontName = typedArray.getString(R.styleable.CustomFontTypeface_typeface);
            setTypeface(StaticSingletonCustomFonts.getTypeface(fontName));

            typedArray.recycle();
        }
    }
}
