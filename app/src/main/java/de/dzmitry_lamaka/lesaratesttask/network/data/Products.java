package de.dzmitry_lamaka.lesaratesttask.network.data;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Products {
    @NonNull
    private List<Product> products;
    @SerializedName("number_products")
    private int numberProducts;
    @SerializedName("number_pages")
    private int numberPages;
    @SerializedName("current_page")
    private int currentPage;

    @NonNull
    public List<Product> getProducts() {
        return products;
    }

    public int getNumberProducts() {
        return numberProducts;
    }

    public int getNumberPages() {
        return numberPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
