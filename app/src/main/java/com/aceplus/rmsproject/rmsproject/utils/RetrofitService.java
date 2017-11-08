package com.aceplus.rmsproject.rmsproject.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aceplus.rmsproject.rmsproject.MainActivity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dell on 9/15/2017.
 */

public class RetrofitService {
    public static OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS);
    public static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("http://aceplusactivation.com")
            .addConverterFactory(GsonConverterFactory.create());

    public static <T> T createService(Class<T> serviceClass) {

        if(!httpClient.interceptors().isEmpty()) {
            httpClient.interceptors().clear();
        }

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(loggingInterceptor);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request origial = chain.request();
                Request.Builder requstbuilder = origial.newBuilder();

                Request request = requstbuilder.build();
                return chain.proceed(request);
            }
        });
        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static <T> T createRetrofitService(Class<T> serviceClass, final Context context) {
        if(!httpClient.interceptors().isEmpty()) {
            httpClient.interceptors().clear();
        }

        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("X-Authorization", getActivateKeyFromDB(context)).build();
                return chain.proceed(newRequest);
            }
        };

        SharedPreferences prefs;
        prefs = context.getSharedPreferences("MYPRE", context.MODE_PRIVATE);
        String BACKEND_URL = "";

        if ( prefs.getString("BACKEND_URL","")!=null) {
            BACKEND_URL = prefs.getString("BACKEND_URL", "");
        }

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        builder.readTimeout(180, TimeUnit.SECONDS);
        builder.connectTimeout(180, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BACKEND_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(serviceClass);
    }

    private static String getActivateKeyFromDB(Context context) { // for activation key

        SQLiteDatabase database = new Database(context).getDataBase();;
        database.beginTransaction();
        String backend_activate_key = null;
        Cursor cur = database.rawQuery("SELECT * FROM activate_key", null);
        while (cur.moveToNext()) {
            backend_activate_key = cur.getString(cur.getColumnIndex("backend_activation_key"));
        }
        cur.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return backend_activate_key;
    }

}
