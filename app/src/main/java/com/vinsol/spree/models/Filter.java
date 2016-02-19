package com.vinsol.spree.models;

/**
 * Created by vaibhav on 12/8/15.
 */

import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class Filter implements Parcelable {
    @SerializedName("values")
    @Expose
     ArrayList<String> values;

    @SerializedName("name")
    @Expose
     String name;

    @SerializedName("filter_type")
    @Expose
     String filterType;

    @SerializedName("search_key")
    @Expose
     String searchKey;

    public Filter() {
    }

    public ArrayList<String> getValues() {
        return values;
    }

    public void setValues(ArrayList<String> values) {
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    protected Filter(Parcel in) {
        if (in.readByte() == 0x01) {
            values = new ArrayList<String>();
            in.readList(values, String.class.getClassLoader());
        } else {
            values = null;
        }
        name = in.readString();
        filterType = in.readString();
        searchKey = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (values == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(values);
        }
        dest.writeString(name);
        dest.writeString(filterType);
        dest.writeString(searchKey);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Filter> CREATOR = new Parcelable.Creator<Filter>() {
        @Override
        public Filter createFromParcel(Parcel in) {
            return new Filter(in);
        }

        @Override
        public Filter[] newArray(int size) {
            return new Filter[size];
        }
    };
}

