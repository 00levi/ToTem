package com.axiel7.tioanime.network;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://raw.githubusercontent.com/00levi/listModTA/refs/heads/main/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {

            // Interceptor para forzar no usar caché y agregar un parámetro único en cada request
            Interceptor noCacheInterceptor = chain -> {
                Request originalRequest = chain.request();

                // Agregar parámetro "nocache" para evitar versiones viejas
                HttpUrl newUrl = originalRequest.url().newBuilder()
                        .addQueryParameter("nocache", String.valueOf(System.currentTimeMillis()))
                        .build();

                Request newRequest = originalRequest.newBuilder()
                        .url(newUrl)
                        .header("Cache-Control", "no-cache")
                        .build();

                return chain.proceed(newRequest);
            };

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(noCacheInterceptor)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
