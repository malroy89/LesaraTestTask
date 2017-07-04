package de.dzmitry_lamaka.lesaratesttask.ui;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import de.dzmitry_lamaka.lesaratesttask.network.data.Product;

class State {
    private static final int STATE_LOADING = 0;
    private static final int STATE_LOADED = 1;
    private static final int STATE_ERROR = 2;
    @Nullable
    private final List<Product> products;
    @Nullable
    private final String errorMessage;
    private final int state;

    private State(@Nullable final List<Product> products, @Nullable final String errorMessage, final int state) {
        this.products = products != null ? Collections.unmodifiableList(products) : null;
        this.errorMessage = errorMessage;
        this.state = state;
    }

    @Nullable
    List<Product> getProducts() {
        return products;
    }

    @Nullable
    String getErrorMessage() {
        return errorMessage;
    }

    boolean isLoading() {
        return state == STATE_LOADING;
    }

    boolean isLoaded() {
        return state == STATE_LOADED;
    }

    boolean isError() {
        return state == STATE_ERROR;
    }

    static State loading() {
        return new State(null, null, STATE_LOADING);
    }

    static State loaded(@NonNull final List<Product> products) {
        return new State(products, null, STATE_LOADED);
    }

    static State error(@NonNull final String errorMessage) {
        return new State(null, errorMessage, STATE_ERROR);
    }

}
