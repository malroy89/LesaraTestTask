package de.dzmitry_lamaka.lesaratesttask.di;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.dzmitry_lamaka.lesaratesttask.network.RestApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
class AppModule {

    @Singleton
    @Provides
    RestApi provideRestApi(@NonNull final OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("https://test4.lesara.de/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(RestApi.class);
    }

    @Singleton
    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Singleton
    @Provides
    OkHttpClient provideOkHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.interceptors().add(httpLoggingInterceptor);
        return client.build();
    }

}
