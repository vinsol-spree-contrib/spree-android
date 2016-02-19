package com.vinsol.spree.controllers;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.squareup.okhttp.ResponseBody;
import com.vinsol.spree.R;
import com.vinsol.spree.api.ApiClient;
import com.vinsol.spree.api.models.HomeResponse;
import com.vinsol.spree.api.models.TaxonomiesResponse;
import com.vinsol.spree.cache.Cache;
import com.vinsol.spree.models.Config;
import com.vinsol.spree.models.Country;
import com.vinsol.spree.models.Taxonomy;
import com.vinsol.spree.utils.Log;
import com.vinsol.spree.utils.SharedPreferencesHelper;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private CoordinatorLayout container;

    private Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // To make activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        container   = (CoordinatorLayout) findViewById(R.id.activity_splash_container);
        progressBar = (ProgressBar)       findViewById(R.id.activity_splash_pb);

        loadAndParseConfig();
    }

    private void loadAndParseConfig() {
        Call<Config> call = ApiClient.getInstance().getApiService().getConfig();
        call.enqueue(new Callback<Config>() {

            @Override
            public void onResponse(Response<Config> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.d("config received");
                    config = response.body();

                    getTaxonomyData();
                } else {
                    int statusCode = response.code();
                    Log.d("Config data not received. Error code : " + statusCode);
                    ResponseBody errorBody = response.errorBody();
                    handleError();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("config data request failed");
                handleError();
            }
        });
    }

    private void getTaxonomyData() {
        Cache cache = Cache.get();
        if (config.getTaxonomiesChecksum().equals(cache.getTaxonomiesChecksum())) { // if taxonomy data has not changed

            getDashboardData();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        Call<TaxonomiesResponse> call = ApiClient.getInstance().getApiService().getAllTaxonomies();
        call.enqueue(new Callback<TaxonomiesResponse>() {
            @Override
            public void onResponse(Response<TaxonomiesResponse> response, Retrofit retrofit) {
                Log.d("Taxonomies data request succeeded");
                if (response.isSuccess()) {
                    Log.d("Taxonomies received");
                    TaxonomiesResponse taxonomiesResponse = response.body();
                    List<Taxonomy> taxonomies = taxonomiesResponse.getTaxonomies();
                    Cache cache = Cache.get();
                    cache.getCachedTaxonomies().clear();
                    cache.getCachedTaxonomies().addAll(taxonomies);
                    cache.setTaxonomiesChecksum(taxonomiesResponse.getChecksum());
                    SharedPreferencesHelper.saveCache(cache);

                    getDashboardData();
                } else {
                    int statusCode = response.code();
                    Log.d("Taxonomies not received. Error code : " + statusCode);
                    ResponseBody errorBody = response.errorBody();
                    Log.d("Tax Error : " + errorBody.toString());
                    handleError();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Taxonomies data request failed");
                handleError();
            }
        });

    }

    private void getDashboardData() {
        Cache cache = Cache.get();
        if (config.getHomeChecksum().equals(cache.getHomeChecksum())) { // if home data has not changed

            getStateList();
            return;
        }

        Call<HomeResponse> call = ApiClient.getInstance().getApiService().getHome();
        call.enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Response<HomeResponse> response, Retrofit retrofit) {
                Log.d("Home data request succeeded");
                if (response.isSuccess()) {
                    Log.d("Home data received");
                    HomeResponse homeResponse = response.body();
                    Cache cache = Cache.get();
                    cache.getDashboardData().clear();
                    cache.getDashboardData().addAll(homeResponse.getBannerTypes());
                    cache.setHomeChecksum(homeResponse.getChecksum());
                    SharedPreferencesHelper.saveCache(cache);

                    getStateList();

                } else {
                    int statusCode = response.code();
                    Log.d("Home data not received. Error code : " + statusCode);
                    ResponseBody errorBody = response.errorBody();
                    Log.d("Home Error : " + errorBody.toString());
                    handleError();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Home data request failed");
                handleError();
            }
        });
    }

    private void getStateList() {
        Cache cache = Cache.get();
        if (config.getStatesChecksum().equals(cache.getStatesChecksum())) { // if state data has not changed

            goToHome();
            return;
        }


        Call<Country> call = ApiClient.getInstance().getApiService().getStates();
        call.enqueue(new Callback<Country>() {
            @Override
            public void onResponse(Response<Country> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.d("State list received");
                    Country country = response.body();
                    Cache cache = Cache.get();
                    cache.getStatesList().clear();
                    cache.getStatesList().addAll(country.getStates());
                    cache.setStatesChecksum(country.getChecksum());
                    SharedPreferencesHelper.saveCache(cache);

                    goToHome();

                } else {
                    int statusCode = response.code();
                    Log.d("State list data not received. Error code : " + statusCode);
                    ResponseBody errorBody = response.errorBody();
                    Log.d("State list Error : " + errorBody.toString());
                    handleError();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("State list data request failed");
                handleError();
            }
        });
    }

    private void goToHome() {
        progressBar.setVisibility(View.GONE);
        Intent openHome = new Intent(SplashActivity.this, Home.class);
        startActivity(openHome);
        finish();
    }

    private void handleError() {
        progressBar.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(container, "Check your Internet Connection", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadAndParseConfig();
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.show();
    }
}
