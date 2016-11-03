package com.jju.yuxin.memo.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.memo.utils
 * Created by yuxin.
 * Created time 2016/11/2 0002 下午 3:55.
 * Version   1.0;
 * Describe : 流的操作
 * History:
 * ==============================================================================
 */

public class SteamUtils {

    /**
     * 字节流解析
     * @param is
     * @return
     */
    public static String getbyteString(InputStream is) {
        String info = null;
        byte[] buffer = new byte[1024];
        int readcount = -1;
        StringBuilder builder = new StringBuilder();
        try {
            while ((readcount = is.read(buffer)) != -1) {
                builder.append(new String(buffer, 0, readcount));
            }
            info = builder.toString();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * 字符流解析
     * @param is
     * @return
     */
    public static String getcharString(InputStream is) {
        String info = null;
        StringBuilder builder = new StringBuilder();
        String line = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        InputStreamReader in = new InputStreamReader(bis);
        BufferedReader reader = new BufferedReader(in);
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            info = builder.toString();
            reader.close();
            in.close();
            bis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }
}
