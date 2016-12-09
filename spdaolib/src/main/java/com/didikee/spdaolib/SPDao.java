package com.didikee.spdaolib;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.didikee.spdaolib.bizs.SPValue;
import com.didikee.spdaolib.interf.ISPDao;

/**
 * Created by didik on 2016/12/8.
 */

public final class SPDao implements ISPDao {

    private final Context context;
    public static final String SPDAO = "spDao";
    public static final String SPKEY = "spKey";

    public SPDao(Context context) {
        this.context = context;
    }

    @Override
    public char getValue(int key) {
        SharedPreferences sp = context.getSharedPreferences(SPDAO,
                Context.MODE_PRIVATE);
        String allValues = sp.getString(SPKEY, "");
        Log.e("test","get: "+allValues);
        String defaultStr = String.valueOf(SPValue.DEFAULT);
        int maxIndex = allValues.length() - 1;
        if (key > maxIndex) {
            SharedPreferences.Editor editor = sp.edit();
            String tempAdd = "";
            for (int i = maxIndex + 1; i <= key; i++) {
                tempAdd += defaultStr;
            }
            editor.putString(SPKEY, allValues + tempAdd);
            editor.apply();
            return SPValue.DEFAULT;
        } else {
            return allValues.charAt(key);
        }
    }

    @Override
    public void putValue(int key, char value) {
        SharedPreferences sp = context.getSharedPreferences(SPDAO,
                Context.MODE_PRIVATE);
        String allValues = sp.getString(SPKEY, "");
        Log.e("test","put start: "+allValues);
        String defaultStr = String.valueOf(SPValue.DEFAULT);
        SharedPreferences.Editor editor = sp.edit();
        int maxIndex = allValues.length() - 1;
        if (key > maxIndex) {
            String tempAdd = "";
            for (int i = maxIndex + 1; i <= key; i++) {
                tempAdd += defaultStr;
            }
            Log.e("test","put end1: "+allValues+ tempAdd);
            editor.putString(SPKEY, allValues+tempAdd);
        } else {
            char oldChar = allValues.charAt(key);
            Log.e("test","oldCar: "+oldChar);
            StringBuilder sb=new StringBuilder(allValues);
            sb.setCharAt(key,value);
            Log.e("test","put end2: "+ sb.toString());
            editor.putString(SPKEY, sb.toString());
        }
        editor.apply();
    }


}
