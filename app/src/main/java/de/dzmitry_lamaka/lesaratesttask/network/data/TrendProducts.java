package de.dzmitry_lamaka.lesaratesttask.network.data;

import com.google.gson.annotations.SerializedName;

public class TrendProducts {
    @SerializedName("trend_products")
    private Products trendProducts;

    public Products getTrendProducts() {
        return trendProducts;
    }
}
