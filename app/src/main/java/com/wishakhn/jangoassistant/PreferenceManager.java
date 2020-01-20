package com.wishakhn.jangoassistant;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    static SharedPreferences prefs;
     static final String PREFERNCE_NAME="SHARED_PREFERENCE";
    static final String KEY_PREFERNCE_NAME="KEY_SHARED_PREFERENCE";

    public static void saveTargetClass(Context context, Class target){
        prefs = context.getSharedPreferences(PREFERNCE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_PREFERNCE_NAME,target.toString());
        editor.apply();
    }
}
