package com.jju.yuxin.memo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jju.yuxin.memo.R;
import com.jju.yuxin.memo.bean.UserBean;
import com.jju.yuxin.memo.service.HttpUtils;
import com.jju.yuxin.memo.utils.JsonParser;
import com.jju.yuxin.memo.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static android.util.Log.e;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName MainActivity
 * Created by yuxin.
 * Created time 03-11-2016 09:34.
 * Describe :
 * History:登录界面
 * Version   1.0.
 *
 *==============================================================================
 */
public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int DO_LOGIN = 1;
    private EditText etname;
    private EditText rtpsd;
    private Button btlogin;
    private Button btregister;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //登录操作
                case DO_LOGIN:
                   UserBean user= (UserBean) msg.obj;
                    e(TAG, "handleMessage" + user.toString());
                    if ("无法连接到服务器!".equals(user.getTodo())||"登录失败".equals(user.getTodo())||user==null){
                        Toast.makeText(MainActivity.this, user.getTodo()+"", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent loginintent = new Intent(MainActivity.this,MemoListActivity.class);
                        loginintent.putExtra("user",user);
                        startActivity(loginintent);
                        finish();
                    }
                    pb_pressbar.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }


        }
    };
    private ProgressBar pb_pressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initialize();
        initializeListener();
        Intent intent = getIntent();
        UserBean user = intent.getParcelableExtra("user");
        if (user != null) {
            etname.setText(user.getName());
            rtpsd.setText(user.getPsd());
        }
    }

    /**
     * 控件与数据的初始化
     */
    private void initialize() {
        etname = (EditText) findViewById(R.id.et_name);
        rtpsd = (EditText) findViewById(R.id.rt_psd);
        btlogin = (Button) findViewById(R.id.bt_login);
        btregister = (Button) findViewById(R.id.bt_register);
        pb_pressbar = (ProgressBar) findViewById(R.id.pb_pressbar);
        pb_pressbar.setVisibility(View.INVISIBLE);
    }

    /**
     * 监听事件的初始化
     */
    private void initializeListener() {

        //注册按钮监听事件
        btregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerintent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(registerintent);
                finish();
            }
        });

        //登录按钮监听事件
        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb_pressbar.setVisibility(View.VISIBLE);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Map<String, Object> map = new HashMap<>();
                        String name = etname.getText().toString().trim();
                        String psd = rtpsd.getText().toString().trim();
                        map.put("name", name);
                        map.put("psd", psd);
                        String registerServlet = HttpUtils.doPost(StringUtils.DOLOGIN, map);
                        UserBean userbean = JsonParser.parseruser(registerServlet);
                        userbean.setTodo(registerServlet);
                        Message msg = Message.obtain();
                        msg.what = DO_LOGIN;
                        msg.obj = userbean;
                        mhandler.sendMessage(msg);
                    }
                }.start();
            }
        });

    }

}
