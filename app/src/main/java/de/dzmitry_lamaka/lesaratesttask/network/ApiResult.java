package de.dzmitry_lamaka.lesaratesttask.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

class ApiResult<T> {
    @Nullable
    private final T data;
    @Nullable
    private final String error;

    private ApiResult(@Nullable final T data, @Nullable final String error) {
        this.data = data;
        this.error = error;
    }

    boolean isSuccess() {
        return TextUtils.isEmpty(error) && data != null;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    String getError() {
        return error;
    }

    @NonNull
    static <T> ApiResult<T> success(@NonNull final T data) {
        return new ApiResult<>(data, null);
    }

    @NonNull
    static <T> ApiResult<T> error(@NonNull final String error) {
        return new ApiResult<>(null, error);
    }
}
