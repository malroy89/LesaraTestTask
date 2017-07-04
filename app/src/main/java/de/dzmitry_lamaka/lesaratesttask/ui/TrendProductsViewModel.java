package de.dzmitry_lamaka.lesaratesttask.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.util.List;

import javax.inject.Inject;

import de.dzmitry_lamaka.lesaratesttask.LesaraTestTaskApplication;
import de.dzmitry_lamaka.lesaratesttask.network.data.Product;
import de.dzmitry_lamaka.lesaratesttask.di.AppComponent;
import de.dzmitry_lamaka.lesaratesttask.network.ProductsLoader;

public class TrendProductsViewModel extends ViewModel implements ProductsLoader.Listener {
    @Inject
    @Nullable
    ProductsLoader productsLoader;
    @NonNull
    private final MutableLiveData<State> stateLiveData;

    public TrendProductsViewModel() {
        stateLiveData = new MutableLiveData<>();
        inject();
        if (productsLoader != null) {
            productsLoader.setListener(this);
        }
    }

    @VisibleForTesting
    TrendProductsViewModel(@NonNull final ProductsLoader productsLoader, @NonNull final MutableLiveData<State> stateLiveData) {
        this.productsLoader = productsLoader;
        this.stateLiveData = stateLiveData;
    }

    private void inject() {
        final AppComponent appComponent = LesaraTestTaskApplication.getAppComponent();
        if (appComponent != null) {
            appComponent.inject(this);
        }
    }

    @MainThread
    void loadNextPage() {
        if (productsLoader == null) {
            throw new IllegalStateException("Forgot to provide dependencies");
        }
        stateLiveData.setValue(State.loading());
        productsLoader.loadNextPage();
    }

    @NonNull
    LiveData<State> getState() {
        return stateLiveData;
    }

    @MainThread
    @Override
    public void onLoaded(@NonNull final List<Product> products) {
        stateLiveData.setValue(State.loaded(products));
    }

    @MainThread
    @Override
    public void onError(@NonNull final String message) {
        stateLiveData.setValue(State.error(message));
    }
}
