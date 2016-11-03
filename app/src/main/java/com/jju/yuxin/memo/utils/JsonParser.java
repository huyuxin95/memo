package com.jju.yuxin.memo.utils;

import android.util.Log;

import com.jju.yuxin.memo.bean.MemoBean;
import com.jju.yuxin.memo.bean.UserBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;


/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.memo.utils
 * Created by yuxin.
 * Created time 2016/11/2 0002 下午 4:48.
 * Version   1.0;
 * Describe : JSON的解析
 * History:
 * ==============================================================================
 */

public class JsonParser {

    private static  final String TAG=JsonParser.class.getSimpleName();

    /**
     * 解析出用户bean
     * @param json
     * @return
     */
    public static UserBean parseruser(String json) {
        UserBean user = new UserBean(0, "", "");
        try {
            JSONObject object = new JSONObject(json);
            int userId = object.getInt("userId");
            String userName = object.getString("userName");
            String psd = object.getString("userPsd");

            user.setId(userId);
            user.setName(userName);
            user.setPsd(psd);
        } catch (JSONException e) {
            Log.e(TAG, "parseruser:Json解析出现问题:" + e.getMessage());;
        }
        return user;
    }

    /**
     * 解析出当前用户的所有备忘录信息
     * @param name
     * @param json
     * @return
     */
    public static List<MemoBean> parserMemo(String name,String json) {
        List<MemoBean> memos = null;
        List<MemoBean> authormemo = null;
        try {
            JSONArray array = new JSONArray(json);
            memos = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String book_title = jsonObject.getString("book_title");
                String book_content = jsonObject.getString("book_content");
                String book_time = jsonObject.getString("book_time");
                String author = jsonObject.getString("author");
                MemoBean memobean = new MemoBean(id, book_title, book_content, book_time, author);
                memos.add(memobean);
            }
            authormemo = new ArrayList<>();
            for (MemoBean memo:memos) {
                if (name.equals(memo.getAuthor())){
                    authormemo.add(memo);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return authormemo;
    }
}
