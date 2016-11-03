package com.jju.yuxin.memo.service;

import com.jju.yuxin.memo.utils.SteamUtils;
import com.jju.yuxin.memo.utils.StringUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.util.Log.e;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.memo.service
 * Created by yuxin.
 * Created time 2016/11/2 0002 下午 3:40.
 * Version   1.0;
 * Describe : 网络连接操作
 * History:
 * ==============================================================================
 */

public class HttpUtils {
    //http://169.254.63.97:8080/BookBox/servlet/RegisterServlet?name=testhu&psd=1234
    //http://169.254.63.97:8080/BookBox/drawable/news_1.jpg
    private final static String path = StringUtils.path;
    private static final String TAG = HttpUtils.class.getSimpleName();

    /**
     * get请求
     * @param servletName
     * @param oMap
     * @return
     */
    public static String doGet(final String servletName, final Map<String, Object> oMap) {
        String info = null;

        try {
            StringBuffer stringBuffer = new StringBuffer(path);
            stringBuffer.append(servletName);
            stringBuffer.append("?");
            if (oMap != null) {
                for (Map.Entry<String, Object> map : oMap.entrySet()) {
                    stringBuffer.append(map.getKey()).append("=").append(map.getValue());
                    stringBuffer.append("&");
                }
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);

            e(TAG, "run" + stringBuffer.toString());

            HttpClient hc = new DefaultHttpClient();
            hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);
            hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,5000);
            HttpGet httpget = new HttpGet(stringBuffer.toString());
            HttpResponse response = hc.execute(httpget);
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                InputStream is = response.getEntity().getContent();
                // e(TAG, "return" + SteamUtils.getbyteString(is));
                info = SteamUtils.getbyteString(is);
            }else{
                info="无法连接到服务器!";
            }

        } catch (Exception e) {
            e.printStackTrace();
            info="无法连接到服务器!";
        }
        return info;
    }


    /**
     * post请求
     * @param servletName
     * @param oMap
     * @return
     */
    public static String doPost(final String servletName, final Map<String, Object> oMap) {
        String info=null;
        try {
            StringBuffer stringBuffer = new StringBuffer(path);
            stringBuffer.append(servletName);
            List<NameValuePair> oList = new ArrayList<NameValuePair>();
            for (Map.Entry<String, Object> entry : oMap.entrySet()) {
                String key = entry.getKey();
                String values = (String) entry.getValue();
                NameValuePair pair = new BasicNameValuePair(key, values);
                oList.add(pair);
            }
            HttpClient hc = new DefaultHttpClient();
            hc.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);
            hc.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,5000);
            HttpPost httpPost = new HttpPost(stringBuffer.toString());
            HttpEntity entity = new UrlEncodedFormEntity(oList, "UTF-8");
            httpPost.setEntity(entity);
            HttpResponse response = hc.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if (code==200){
                InputStream is = response.getEntity().getContent();
                info = SteamUtils.getbyteString(is);
            }else{
                info="无法连接到服务器!";
            }
        } catch (Exception e) {
            e.printStackTrace();
            info="无法连接到服务器!";
        }
        return info;
    }
}
