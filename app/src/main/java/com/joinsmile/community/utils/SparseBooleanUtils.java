package com.joinsmile.community.utils;

import android.util.SparseBooleanArray;

import java.util.List;

/**
 * Created by liuguofeng719 on 2016/7/20.
 */
public class SparseBooleanUtils {

    private static SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    public static SparseBooleanArray getSparseBooleanArray(){
        return sparseBooleanArray;
    }

    public static void setData(List<Boolean> booleanList) {
        for (int i = 0; i < booleanList.size(); i++) {
            sparseBooleanArray.put(i, booleanList.get(i));
        }
    }

    public static void resetBoolean() {
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            sparseBooleanArray.put(i, false);
        }
    }

    public static void putBoolean(int key, boolean value) {
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            if (i == key) {
                sparseBooleanArray.put(i, value);
                break;
            }
        }
    }
}
