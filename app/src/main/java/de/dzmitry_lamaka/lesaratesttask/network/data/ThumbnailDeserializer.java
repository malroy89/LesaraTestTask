package de.dzmitry_lamaka.lesaratesttask.network.data;

import android.support.annotation.NonNull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

class ThumbnailDeserializer implements JsonDeserializer<String> {
    @NonNull
    private static final String IMAGE_URL_FORMATTER = "https://daol3a7s7tps6.cloudfront.net/%s";

    @Override
    public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return String.format(IMAGE_URL_FORMATTER, json.getAsString());
    }
}
