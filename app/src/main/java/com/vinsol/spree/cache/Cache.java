package com.vinsol.spree.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vinsol.spree.models.BannerTypes;
import com.vinsol.spree.models.Product;
import com.vinsol.spree.models.State;
import com.vinsol.spree.models.Taxonomy;
import com.vinsol.spree.models.User;
import com.vinsol.spree.utils.SharedPreferencesHelper;
import com.vinsol.spree.utils.Strings;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by vaibhav on 10/20/15.
 */
public class Cache {

    private static Cache CACHE;

    public static Cache get() {
        if (!SharedPreferencesHelper.isCacheAvailable()) {
            CACHE = new Cache();
            SharedPreferencesHelper.saveCache(CACHE);
        } else {
            CACHE = SharedPreferencesHelper.getCache();
        }

        return CACHE;
    }


    private String taxonomiesChecksum;
    private String statesChecksum;
    private String homeChecksum;

    private ArrayList<Taxonomy> cachedTaxonomies;
    private ArrayList<Product> recentlyViewedProducts;
    private ArrayList<BannerTypes> dashboardData;
    private ArrayList<State> statesList;
    private User user;

    public Cache() {
        cachedTaxonomies = new ArrayList<>();
        recentlyViewedProducts  = new ArrayList<>();
        dashboardData = new ArrayList<>();
        statesList = new ArrayList<>();
    }

    public String getTaxonomiesChecksum() {
        return Strings.nullSafeString(taxonomiesChecksum);
    }

    public void setTaxonomiesChecksum(String taxonomiesChecksum) {
        this.taxonomiesChecksum = taxonomiesChecksum;
    }

    public String getStatesChecksum() {
        return Strings.nullSafeString(statesChecksum);
    }

    public void setStatesChecksum(String statesChecksum) {
        this.statesChecksum = statesChecksum;
    }

    public String getHomeChecksum() {
        return Strings.nullSafeString(homeChecksum);
    }

    public void setHomeChecksum(String homeChecksum) {
        this.homeChecksum = homeChecksum;
    }

    public ArrayList<Taxonomy> getCachedTaxonomies() {
        return cachedTaxonomies;
    }

    public ArrayList<Product> getRecentlyViewedProducts() {
        return recentlyViewedProducts;
    }

    public ArrayList<BannerTypes> getDashboardData() {
        return dashboardData;
    }

    public ArrayList<State> getStatesList() {
        return statesList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static String serialize(Cache cache) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.enableComplexMapKeySerialization().setPrettyPrinting().create();
        return gson.toJson(cache);
    }

    public static Cache deserialize(String json) {
        Type type = new TypeToken<Cache>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    public void update() {
        SharedPreferencesHelper.saveCache(this);
    }
}
