package de.dzmitry_lamaka.lesaratesttask.network;

import android.support.annotation.NonNull;

import java.io.IOException;

import javax.inject.Inject;

import de.dzmitry_lamaka.lesaratesttask.network.data.Products;
import de.dzmitry_lamaka.lesaratesttask.network.data.TrendProducts;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class RestCaller {
    @NonNull
    private final RestApi restApi;

    @Inject
    public RestCaller(@NonNull final RestApi restApi) {
        this.restApi = restApi;
    }

    @NonNull
    ApiResult<Products> getTrendProducts(final int pageToLoad) {
        final Call<TrendProducts> trendProductsCall = restApi.getTrendProducts(pageToLoad);
        try {
            final Response<TrendProducts> response = trendProductsCall.execute();
            if (response.isSuccessful()) {
                final TrendProducts trendProducts = response.body();
                if (trendProducts == null) {
                    throw new IOException("Error parsing body");
                }
                return ApiResult.success(trendProducts.getTrendProducts());
            } else {
                final ResponseBody responseBody = response.errorBody();
                if (responseBody == null) {
                    throw new IOException("Error parsing body");
                }
                return ApiResult.error(responseBody.string());
            }
        } catch (@NonNull final IOException e) {
            return ApiResult.error(e.getMessage());
        }
    }
}
