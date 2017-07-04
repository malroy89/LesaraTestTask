package de.dzmitry_lamaka.lesaratesttask.network;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import de.dzmitry_lamaka.lesaratesttask.network.data.Product;
import de.dzmitry_lamaka.lesaratesttask.network.data.Products;

public class ProductsLoader {
    @NonNull
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    @NonNull
    private final RestCaller restCaller;
    @NonNull
    private final Executor executor;
    @Nullable
    private Listener listener;
    @NonNull
    private final AtomicInteger nextPage;
    @NonNull
    private final AtomicInteger totalPages;
    @NonNull
    private final Runnable task = new Runnable() {

        @WorkerThread
        @Override
        public void run() {
            final ApiResult<Products> apiResult = restCaller.getTrendProducts(nextPage.intValue());
            if (apiResult.isSuccess()) {
                final Products trendProducts = apiResult.getData();
                if (trendProducts != null) {
                    nextPage.set(trendProducts.getCurrentPage() + 1);
                    totalPages.set(trendProducts.getNumberPages());
                    callOnLoaded(trendProducts.getProducts());
                }
            } else {
                final String error = apiResult.getError();
                if (error != null) {
                    callOnError(error);
                }
            }
        }
    };

    @Inject
    ProductsLoader(@NonNull final RestCaller restCaller, @NonNull final Executor executor) {
        this(restCaller, executor, new AtomicInteger(1), new AtomicInteger());
    }

    @VisibleForTesting
    ProductsLoader(@NonNull final RestCaller restCaller, @NonNull final Executor executor, @NonNull final AtomicInteger nextPage,
                   @NonNull final AtomicInteger totalPages) {
        this.restCaller = restCaller;
        this.executor = executor;
        this.nextPage = nextPage;
        this.totalPages = totalPages;
    }

    public void setListener(@Nullable final Listener listener) {
        this.listener = listener;
    }

    @MainThread
    public void loadNextPage() {
        final int nextPage = this.nextPage.intValue();
        final int totalPages = this.totalPages.intValue();
        if (nextPage == totalPages) {
            callOnLoaded(Collections.<Product>emptyList());
            return;
        }
        executor.execute(task);
    }

    private void callOnLoaded(@NonNull final List<Product> products) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onLoaded(products);
                }
            }
        });
    }

    private void callOnError(@NonNull final String message) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onError(message);
                }
            }
        });
    }

    public interface Listener {
        @MainThread
        void onLoaded(@NonNull List<Product> products);

        @MainThread
        void onError(@NonNull String message);
    }

}
