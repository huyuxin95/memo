package com.jju.yuxin.memo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jju.yuxin.memo.R;
import com.jju.yuxin.memo.bean.UserBean;
import com.jju.yuxin.memo.service.HttpUtils;
import com.jju.yuxin.memo.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName RegisterActivity
 * Created by yuxin.
 * Created time 03-11-2016 09:37.
 * Describe :注册界面
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class RegisterActivity extends Activity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final int DO_REGISTER = 1;
    private EditText etregistername;
    private EditText rtregisterpsd;
    private Button btdoregister;

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //注册操作
                case DO_REGISTER:
                    UserBean user= (UserBean) msg.obj;
                    if ("注册成功".equals(user.getTodo())) {
                        Toast.makeText(RegisterActivity.this, "注册成功!!", Toast.LENGTH_SHORT).show();
                        Intent doregisterintent = new Intent(RegisterActivity.this, MainActivity.class);
                        doregisterintent.putExtra("user",user);
                        startActivity(doregisterintent);
                        finish();
                    } else {
                        Log.e(TAG, "handleMessage" +  user.getTodo());
                        Toast.makeText(RegisterActivity.this, user.getTodo()+"", Toast.LENGTH_SHORT).show();
                    }
                    pb_re_pressbar.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    };
    private ProgressBar pb_re_pressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        initialize();
        initializeListener();
    }


    /**
     * 数据与控件的初始化
     */
    private void initialize() {

        etregistername = (EditText) findViewById(R.id.et_register_name);
        rtregisterpsd = (EditText) findViewById(R.id.rt_register_psd);
        btdoregister = (Button) findViewById(R.id.bt_do_register);
        pb_re_pressbar = (ProgressBar) findViewById(R.id.pb_re_pressbar);
        pb_re_pressbar.setVisibility(View.INVISIBLE);
    }


    /**
     * 控件的初始化
     */
    private void initializeListener() {

        btdoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb_re_pressbar.setVisibility(View.VISIBLE);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Map<String, Object> map = new HashMap<>();
                        String name = etregistername.getText().toString().trim();
                        String psd = rtregisterpsd.getText().toString().trim();
                        map.put("name", name);
                        map.put("psd", psd);
                        String registerServlet = HttpUtils.doPost(StringUtils.DOREGISTER, map);
                        Message msg = Message.obtain();
                        msg.what = DO_REGISTER;
                        msg.obj =new UserBean(name,psd,registerServlet);
                        mhandler.sendMessage(msg);
                    }
                }.start();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent registerintent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(registerintent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
