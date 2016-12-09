package com.didikee.spdaolib.utils;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by didik 
 * Created time 2016/12/9
 * Description: 
 */

public class SPDaoChecker {

    private static final String CHANGE="$change";
    private static final String SERIAL_VERSION_UID="serialVersionUID";

    /**
     * check your SPKeys
     * @param clz class extend SPKey
     * @return
     */
    public static boolean check(Class clz){
        ArrayList<Integer> charList=new ArrayList();
        Field[] fields = clz.getFields();
        for (Field field : fields) {
            String name = field.getName();
            if (SERIAL_VERSION_UID.equalsIgnoreCase(name)
                    || CHANGE.equalsIgnoreCase(name)){
                continue;
            }
            boolean accessible = field.isAccessible();
            if (!accessible)field.setAccessible(true);
            int value= -1;
            try {
                value = field.getInt(clz.newInstance());
            } catch (Exception e){
                Log.d("test","e: "+e.getClass().getSimpleName()+"msg: "+e.getMessage());
                return false;
            }
            if (charList.contains(value)){
                return false;
            }else {
                charList.add(value);
            }
            Log.d("test","name: "+name+"value: "+value);

        }
        return true;
    }
}
