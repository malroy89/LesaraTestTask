package de.dzmitry_lamaka.lesaratesttask.network.data;

import android.support.annotation.Nullable;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

public class Product {
    @Nullable
    private String id;
    @Nullable
    private String name;
    @SerializedName("thumbnail_path")
    @JsonAdapter(ThumbnailDeserializer.class)
    @Nullable
    private String thumbnailPath;
    private float price;

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public float getPrice() {
        return price;
    }

}
