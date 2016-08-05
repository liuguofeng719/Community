package com.community;

import com.joinsmile.community.utils.MD5Util;

import org.junit.Test;

import java.sql.Timestamp;

/**
 * Created by liuguofeng719 on 2016/7/6.
 */
public class Md5 {

    @Test
    public void testMd5() throws Exception {
        long timeMillis = System.currentTimeMillis();
        System.out.println("" + timeMillis);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
        System.out.println(System.nanoTime());
        String utf8 = MD5Util.MD5Encode("123456", "utf8");
        System.out.println(utf8);

        System.out.println(accumulation("97878076386290000004000".toCharArray()));
        System.out.println(9 ^ 7 ^ 8 ^ 7 ^ 8 ^ 0 ^ 7 ^ 6 ^ 3 ^ 8 ^ 6 ^ 2 ^ 9 ^ 0 ^ 0 ^ 0 ^ 0 ^ 0 ^ 0 ^ 4 ^ 0 ^ 0 ^ 0);
        String uiiStr = "978730705381A0800007F000";
        if (uiiStr.length() > 24) {//判断扫描24位和28位
            String strUii = uiiStr.substring(4, uiiStr.length());
            String startStr = strUii.substring(0, 14);
            String verifyCode = strUii.substring(14, 15);
            String endStr = strUii.substring(15, strUii.length());
            int accumulation = accumulation((startStr + endStr).toCharArray());
            if (Integer.parseInt("" + verifyCode, 16) != accumulation) {
                System.out.println("false1");
                return;
            }
        } else {
            String startStr = uiiStr.substring(0, 14);
            String verifyCode = uiiStr.substring(14, 15);
            String endStr = uiiStr.substring(15, uiiStr.length());
            int accumulation = accumulation((startStr + endStr).toCharArray());
            if (Integer.parseInt("" + verifyCode, 16) != accumulation) {
                System.out.println("false2");
                return;
            }
        }
    }

    public static int accumulation(char[] chars) {
        int result = 0;
        for (int i = 0; i < chars.length; i++) {
            result ^= Integer.parseInt("" + chars[i], 16);
        }
        return result;
    }
}
