package com.didikee.spdaolib;

import android.content.Context;
import android.content.SharedPreferences;

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

    private void init() {

    }

    @Override
    public char getValue(int key) {
        SharedPreferences sp = context.getSharedPreferences(SPDAO,
                Context.MODE_PRIVATE);
        String allValues = sp.getString(SPKEY, "");
        int length = allValues.length();
        String defaultStr = String.valueOf(SPValue.DEFAULT);
        if (length==0){
            SharedPreferences.Editor editor = sp.edit();
            String tempAdd="";
            for (int i = 0; i < key; i++) {
                tempAdd+=defaultStr;
            }
            editor.putString(SPKEY, tempAdd);
            editor.apply();
            return SPValue.DEFAULT;
        }else {
            if (key > length -1){
                SharedPreferences.Editor editor = sp.edit();
                int i = key - length - 1;
                String tempAdd="";
                for (int i1 = 0; i1 < i; i1++) {
                    tempAdd+=defaultStr;
                }
                editor.putString(SPKEY, allValues + tempAdd);
                editor.apply();
                return SPValue.DEFAULT;
            }else {
                return allValues.charAt(key);
            }
        }
    }

    @Override
    public boolean putValue(int key, char value) {
        SharedPreferences sp = context.getSharedPreferences(SPDAO,
                Context.MODE_PRIVATE);

        String allValues = sp.getString(SPKEY, "");
        int length = allValues.length();
        if ((key == 0 && length != 0) || (key <= length - 1)) {
            return false;
        } else {
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(SPKEY, allValues + String.valueOf(value));
            editor.apply();
            return true;
        }
    }

}
