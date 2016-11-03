package com.jju.yuxin.memo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jju.yuxin.memo.R;
import com.jju.yuxin.memo.bean.MemoBean;
import com.jju.yuxin.memo.bean.UserBean;
import com.jju.yuxin.memo.service.HttpUtils;
import com.jju.yuxin.memo.utils.StringUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName AddMemoActivity
 * Created by yuxin.
 * Created time 03-11-2016 09:32.
 * Describe :备忘录的添加与修改页面
 * History:
 * Version   1.0.
 *
 *==============================================================================
 */
public class AddMemoActivity extends Activity {

    private static final String TAG = AddMemoActivity.class.getSimpleName();
    private static final int DO_CHANGE = 2;
    private static final int CHANGE_IMAGE = 3;
    private EditText etmemotitle;
    private EditText etmemocontent;
    private Button btdoaddmemo;
    private UserBean user;
    private static final int DO_ADDMEMO = 1;


    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //添加操作
                case DO_ADDMEMO:
                    if ("添加成功".equals((String) msg.obj)) {
                        Toast.makeText(AddMemoActivity.this, "数据插入成功!!", Toast.LENGTH_SHORT).show();
                        Intent doaddmemo = new Intent(AddMemoActivity.this, MemoListActivity.class);
                        doaddmemo.putExtra("user", user);
                        startActivity(doaddmemo);
                        finish();
                    } else {
                        Toast.makeText(AddMemoActivity.this, "数据插入失败!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                //修改操作
                case DO_CHANGE:
                    if ("true".equals((String) msg.obj)) {
                        Toast.makeText(AddMemoActivity.this, "数据修改成功!!", Toast.LENGTH_SHORT).show();
                        Intent doaddmemo = new Intent(AddMemoActivity.this, MemoListActivity.class);
                        doaddmemo.putExtra("user", user);
                        startActivity(doaddmemo);
                        finish();
                    } else {
                        Toast.makeText(AddMemoActivity.this, "数据修改失败!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private MemoBean memo;
    private TextView title_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_memo);
        initialize();
        initializeListener();
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        memo = intent.getParcelableExtra("memo");
        if (memo != null) {
            etmemotitle.setText(memo.getBook_title());
            etmemocontent.setText(memo.getBook_content());
            btdoaddmemo.setText("修改");
            title_msg.setText("修改备忘录");
        }
    }

    /**
     * 控件与数据的初始化
     */
    private void initialize() {

        etmemotitle = (EditText) findViewById(R.id.et_memo_title);
        etmemocontent = (EditText) findViewById(R.id.et_memo_content);
        btdoaddmemo = (Button) findViewById(R.id.bt_do_add_memo);
        title_msg = (TextView) findViewById(R.id.title_msg);

    }

    /**
     * 监听事件的初始化
     */
    private void initializeListener() {
        btdoaddmemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memo != null) {
                    updataMemo();
                } else {
                    addMemo();
                }
            }
        });

    }

    /**
     * 修改备忘录
     */
    private void updataMemo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                //id 唯一标识 book_title 标题 book_content 内容 sendTime 时间 author 作者
                Map<String, Object> map = new HashMap<>();
                map.put("id", memo.getId());
                map.put("book_title", etmemotitle.getText().toString());
                map.put("book_content", etmemocontent.getText().toString());
                map.put("sendTime", memo.getBook_time());
                map.put("author", memo.getAuthor());
                String updateBookServlet = HttpUtils.doGet(StringUtils.DOUPDATAMEMO, map);
                Message msg = Message.obtain();
                msg.what = DO_CHANGE;
                msg.obj = updateBookServlet;
                mhandler.sendMessage(msg);

            }
        }.start();


    }

    /**
     * 添加备忘录
     */
    private void addMemo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Map<String, Object> map = new HashMap<>();
                String title = etmemotitle.getText().toString();
                String content = etmemocontent.getText().toString();
                Calendar calendar = Calendar.getInstance();
                StringBuffer time = new StringBuffer();
                time.append(calendar.get(Calendar.YEAR)).append("-");
                time.append(calendar.get(Calendar.MONTH) + 1).append("-");
                time.append(calendar.get(Calendar.DAY_OF_MONTH)).append("");
                time.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
                time.append(calendar.get(Calendar.MINUTE)).append(":");
                time.append(calendar.get(Calendar.SECOND));
                map.put("book_title", title);
                map.put("book_content", content);
                map.put("sendTime", time.toString());
                map.put("author", user.getName());
                  // String registerServlet = HttpUtils.doGet(StringUtils.DOADDMEMO, map);
                String registerServlet = HttpUtils.doPost("SendBookServlet", map);
                Message msg = Message.obtain();
                msg.what = DO_ADDMEMO;
                msg.obj = registerServlet;
                mhandler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent backmemo = new Intent(AddMemoActivity.this, MemoListActivity.class);
            backmemo.putExtra("user", user);
            startActivity(backmemo);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}
