package com.didikee.spdaolib.interf;

/**
 * Created by didik on 2016/12/8.
 */

public interface ISPDao {
    char getValue(int key);
    boolean putValue(int key,char value);
}
