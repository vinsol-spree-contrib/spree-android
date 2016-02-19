package com.vinsol.spree.utils;

import android.graphics.Typeface;

import com.vinsol.spree.SpreeApplication;

public class StaticSingletonCustomFonts {
	
	private static Typeface robotoLight;
	private static Typeface robotoRegular;
	private static Typeface robotoMedium;
	private static Typeface robotoBold;

	public static Typeface getTypeface(String fontName) {
		if(fontName.equals("roboto_light.ttf")) {
			return (robotoLight == null) ? (robotoLight = initializeTypeface(fontName)) : robotoLight;
		} else if(fontName.equals("roboto_regular.ttf")) {
			return (robotoRegular == null) ? (robotoRegular = initializeTypeface(fontName)) : robotoRegular;
		} else if(fontName.equals("roboto_medium.ttf")) {
			return (robotoMedium == null) ? (robotoMedium = initializeTypeface(fontName)) : robotoMedium;
		} else if(fontName.equals("roboto_bold.ttf")) {
			return (robotoBold == null) ? (robotoBold = initializeTypeface(fontName)) : robotoBold;
		} else {
			Log.d("Font " + fontName + " is not present. Check StaticSingletonCustomFonts class and assets folder.");
			return null;
		}
	}
	
	private static Typeface initializeTypeface(String fontName) {
		return Typeface.createFromAsset(SpreeApplication.getContext().getAssets(), "fonts/" + fontName);
	}
}