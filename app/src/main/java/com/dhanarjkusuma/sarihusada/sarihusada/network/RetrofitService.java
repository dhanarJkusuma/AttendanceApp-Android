package com.dhanarjkusuma.sarihusada.sarihusada.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dhanar J Kusuma on 07/03/2017.
 */

public class RetrofitService
{
    private static ApiService mApiService;
    private static ApiService mApiHeaderService;
    private static Retrofit mRetrofit;
    private static Retrofit mHeaderRetrofit;
    public static ApiService getService(){
        if(mApiService==null){
            Retrofit service = new Retrofit.Builder()
                                            .baseUrl(ApiService.URL_ENDPOINT)
                                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
            mApiService = service.create(ApiService.class);
        }
        return mApiService;
    }

    public static ApiService getApiHeaderService(final String token)
    {
        if(mApiHeaderService == null)
        {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("Authorization", token)
                            .build();

                    return chain.proceed(request);
                }
            });

            httpClient.addInterceptor(logging);

            OkHttpClient client = httpClient.build();
            Retrofit service = new Retrofit.Builder()
                    .baseUrl(ApiService.URL_ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
            mApiHeaderService = service.create(ApiService.class);
        }
        return mApiHeaderService;

    }

    public static Retrofit getRetrofit(){
        return mRetrofit;
    }

    public static Retrofit getHeaderRetrofit(){
        return mHeaderRetrofit;
    }
}
