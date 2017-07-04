package de.dzmitry_lamaka.lesaratesttask;

import android.app.Application;
import android.support.annotation.Nullable;

import de.dzmitry_lamaka.lesaratesttask.di.AppComponent;
import de.dzmitry_lamaka.lesaratesttask.di.DaggerAppComponent;

public class LesaraTestTaskApplication extends Application {
    @Nullable
    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .build();
    }

    @Nullable
    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
