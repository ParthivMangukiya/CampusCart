package com.parthiv.shopper.api;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parthiv.shopper.ShopperApp;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public final class APIClient {

    private static final int CONNECT_TIMEOUT_SECONDS = 15; // 15s

    private static final int READ_TIMEOUT_SECONDS = 15; // 15s
    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String TAG = "APIClient";

    private static CampusCartApi campusCartApi;

    private static ObjectMapper objectMapper;
    private static OkHttpClient.Builder okHttpClientBuilder;
    private static Retrofit.Builder retrofitBuilder;

    static {
        okHttpClientBuilder = new OkHttpClient().newBuilder()
                .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS);

        retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(getObjectMapper()));
    }

    public static ObjectMapper getObjectMapper(){
        if (objectMapper == null){
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        }
        return objectMapper;
    }

    public static OkHttpClient getClient() {
        return okHttpClientBuilder
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BASIC))
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache())
                .build();
    }

    public static Retrofit.Builder getRetrofitBuilder() {
        return retrofitBuilder;
    }

    public static CampusCartApi getCampusCartAPI() {
        if (campusCartApi == null) {
            OkHttpClient okHttpClient = okHttpClientBuilder.addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();

            ObjectMapper objectMapper = getObjectMapper();

            campusCartApi = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create(getObjectMapper()))
                    .baseUrl("http://172.23.0.71/CampusCart/api/v1/")
                    .build()
                    .create(CampusCartApi.class);
        }

        return campusCartApi;
    }

    private static Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(ShopperApp.getAppContext().getCacheDir(), "feed-cache"),
                    10 * 1024 * 1024); // 10 MB
        } catch (Exception e) {
            Log.e(TAG,e.toString());
        }
        return cache;
    }

    private static Interceptor provideCacheInterceptor() {
        return chain -> {
            Response response = chain.proceed(chain.request());

            // re-write response header to force use of cache
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(2, TimeUnit.MINUTES)
                    .build();

            return response.newBuilder()
                    .removeHeader("Pragma")
                    .header(CACHE_CONTROL, cacheControl.toString())
                    .build();
        };
    }

}

