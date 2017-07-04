package de.dzmitry_lamaka.lesaratesttask.network;

import android.text.TextUtils;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

@Implements(TextUtils.class)
public class ShadowTextUtils {

    @Implementation
    public static boolean isEmpty(final String value) {
        return value == null || value.length() == 0;
    }

}
