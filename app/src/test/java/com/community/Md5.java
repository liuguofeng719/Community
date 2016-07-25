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
        System.out.println(""+timeMillis);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
        System.out.println(System.nanoTime());
        String utf8 = MD5Util.MD5Encode("AppGet"+ timestamp.getTime(), "utf8");
        System.out.println(utf8);
    }
}
