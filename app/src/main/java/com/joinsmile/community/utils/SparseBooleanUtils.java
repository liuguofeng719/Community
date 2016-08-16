package com.joinsmile.community.utils;

import android.util.SparseBooleanArray;

/**
 * Created by liuguofeng719 on 2016/7/20.
 */
public class SparseBooleanUtils {

    private static volatile SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    public static synchronized SparseBooleanArray getSparseBooleanArray() {
        return sparseBooleanArray;
    }

    /**
     * 初始所有的值默认都不选中
     *
     * @param count
     */
    public static void setData(int count, boolean value) {
        for (int i = 0; i < count; i++) {
            sparseBooleanArray.put(i, value);
        }
    }

    /**
     * 全选
     */
    public static void checkedAll(boolean value) {
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            sparseBooleanArray.put(i, value);
        }
    }

    /**
     * 反选
     */
    public static void resetBoolean() {
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            sparseBooleanArray.put(i, false);
        }
    }

    /**
     * 设置单个值选中
     *
     * @param key
     * @param value
     */
    public static void putBoolean(int key, boolean value) {
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            if (i == key) {
                sparseBooleanArray.put(i, value);
                break;
            }
        }
    }

    public static void print() {
        TLog.d("sparseBooleanArray===", sparseBooleanArray.toString());
    }
}
