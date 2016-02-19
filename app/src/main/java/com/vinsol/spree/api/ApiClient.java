package com.vinsol.spree.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.vinsol.spree.utils.Constants;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


public class ApiClient {
    public static final String HEADER_API_TOKEN = "X-Spree-Token";
    private static ApiClient apiClient;

    private ApiService apiService;
    private ApiService sessionApiService;

    public static ApiClient getInstance() {
        if(apiClient == null) {
            apiClient = new ApiClient();
        }
        return apiClient;
    }

    public ApiClient() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .enableComplexMapKeySerialization()
                .setPrettyPrinting()
                .create();

        OkHttpClient client = new OkHttpClient();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(loggingInterceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        apiService = retrofit.create(ApiService.class);


        Retrofit sessionRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(createRequestInterceptorClient())
                .build();



        sessionApiService = sessionRetrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return apiService;
    }

    public ApiService getSessionApiService() {
        return sessionApiService;
    }

    private OkHttpClient createRequestInterceptorClient() {
        // Set up system-wide CookieHandler to capture all cookies sent from server.
        final CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        // Set up clientinterceptor to include cookie value in the header.
        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Customize the request
                Request request = original.newBuilder()
                        .addHeader(HEADER_API_TOKEN, "8915ef28cf2a154a74e59ce6aada52d0da969126a32d5455")
                        .build();

                Response response = chain.proceed(request);
                return response;
            }
        });

        return client;
    }
}