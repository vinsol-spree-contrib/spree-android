package com.vinsol.spree.utils;

public class Strings {
	public static final String EMPTY = "";
	
	public static String nullSafeString(String s) {
        return s == null ? EMPTY : s;
    }

	public static boolean isEmpty(String string) {
		return (string == null || string.trim().equals(EMPTY)) ? true : false;
	}

	public static boolean isNotEmpty(String string) {
		return !isEmpty(string);
	}
}
