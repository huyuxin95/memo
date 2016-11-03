package com.jju.yuxin.memo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ProviderInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.jju.yuxin.memo.R;
import com.jju.yuxin.memo.adapter.MemoListAdapter;
import com.jju.yuxin.memo.bean.MemoBean;
import com.jju.yuxin.memo.bean.UserBean;
import com.jju.yuxin.memo.service.HttpUtils;
import com.jju.yuxin.memo.utils.JsonParser;
import com.jju.yuxin.memo.utils.StringUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.ContentValues.TAG;
import static android.util.Log.e;

/**
 *=============================================================================
 *
 * Copyright (c) 2016  yuxin rights reserved.
 * ClassName MemoListActivity
 * Created by yuxin.
 * Created time 03-11-2016 09:37.
 * Describe :
 * History: 备忘录列表界面
 * Version   1.0.
 *
 *==============================================================================
 */
public class MemoListActivity extends Activity {

    private static final int SHOW_LIST = 1;
    private static final int CHANGE_LIST = 0;
    private static final int BITMAP_OK = 2;
    private static final int CHANGE_IMAGE = 3;

    /**
     * 在子线程中执行完需要在主线程执行的操作
     */
    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //显示列表信息
                case SHOW_LIST:
                    memos = (List<MemoBean>) msg.obj;
                    adapter = new MemoListAdapter(MemoListActivity.this, memos);
                    memolistview.setAdapter(adapter);
                    break;
                //列表数据发生改变
                case CHANGE_LIST:
                    String deleteBookServlet = (String) msg.obj;
                    if ("1".equals(deleteBookServlet)) {
                        adapter.removeItme(msg.arg1);
                        adapter.notifyDataSetChanged();
                        e(TAG, deleteBookServlet);
                    }
                    break;
                //图片加载完成
                case BITMAP_OK:
                    bitmaps = (List<Bitmap>) msg.obj;
                    e(TAG, "handleMessage" + "图片加载成功");
                    setImageView();
                    for (int i = 0; i < paths.size(); i++) {
                        inputToOutput(paths.get(i), bitmaps.get(i));
                    }
                    vp_adapter.notifyDataSetChanged();
                    break;
                //图片轮播操作
                case CHANGE_IMAGE:
                    vp_pic.setCurrentItem(index % bitmaps.size());
                    break;
                default:
                    break;
            }

        }
    };
    private List<String> paths = new ArrayList<>();
    private Button addmemo;
    private ListView memolistview;
    private UserBean user;
    private static final String TAG = MemoListActivity.class.getSimpleName();
    private List<MemoBean> memos;
    private MemoListAdapter adapter;
    private ViewPager vp_pic;
    private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    private List<ImageView> oImageViews = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_memo_list);
        initialize();
        initializeListener();
        getusermemo();
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        image_thread();
    }

    /**
     * 控件和数据的初始化
     */
    private void initialize() {
        addmemo = (Button) findViewById(R.id.add_memo);
        memolistview = (ListView) findViewById(R.id.memo_listview);
        vp_pic = (ViewPager) findViewById(R.id.vp_pic);
        paths.add(StringUtils.pic1);
        paths.add(StringUtils.pic2);
        paths.add(StringUtils.pic3);
        paths.add(StringUtils.pic4);
        bitmaps = getBitmaps();
        e(TAG, "initialize" + bitmaps+"size"+bitmaps.size());
        if (bitmaps == null || bitmaps.size() == 0) {
            getPic();
        }
        setImageView();

        vp_pic.setAdapter(vp_adapter);

    }

    /**
     * 将bitmaps赋值给ImageView控件
     */
    private void setImageView(){
        for (int i = 0; i < bitmaps.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(params);
            imageView.setImageBitmap(bitmaps.get(i));
            oImageViews.add(imageView);
        }
    }

    /**
     * 监听事件的初始化
     */
    private void initializeListener() {
        //添加按钮点击事件
        addmemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addintent = new Intent(MemoListActivity.this, AddMemoActivity.class);
                addintent.putExtra("user", user);
                startActivity(addintent);
                finish();
            }
        });
        //item的长按事件
        memolistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final MemoBean memoBean = memos.get(position);
                final AlertDialog.Builder builder = new AlertDialog.Builder(MemoListActivity.this);
                builder.setTitle("提示信息");
                builder.setMessage("是否确认删除?");
                builder.setCancelable(false);
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("id", memoBean.getId());
                                String deleteBookServlet = HttpUtils.doGet(StringUtils.DODELETEMEMO, map);
                                Message msg = Message.obtain();
                                msg.what = CHANGE_LIST;
                                msg.arg1 = position;
                                msg.obj = deleteBookServlet;
                                mhandler.sendMessage(msg);

                            }
                        }.start();

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });

        //ListView的点击事件
        memolistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MemoBean memoBean = memos.get(position);
                Intent updataintent = new Intent(MemoListActivity.this, AddMemoActivity.class);
                updataintent.putExtra("user", user);
                updataintent.putExtra("memo", memoBean);
                startActivity(updataintent);
                finish();
            }
        });
    }

    /**
     * 获取当前用户的备忘录
     */
    private void getusermemo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String showBookListServlet = HttpUtils.doGet(StringUtils.DOSHOWMEMO, null);
                e(TAG, "run" + showBookListServlet);
                List<MemoBean> memoBeen = JsonParser.parserMemo(user.getName(), showBookListServlet);
                Message msg = Message.obtain();
                msg.what = SHOW_LIST;
                msg.obj = memoBeen;
                mhandler.sendMessage(msg);

            }
        }.start();
    }

    /**
     * 获取网络图片
     */
    private void getPic() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    //http://169.254.63.97:8080/BookBox/drawable/news_1.jpg

                    for (int i = 0; i < paths.size(); i++) {
                        HttpClient hc = new DefaultHttpClient();
                        String url = paths.get(i);
                        HttpGet httpget = new HttpGet(url);
                        HttpResponse response = hc.execute(httpget);
                        int statusCode = response.getStatusLine().getStatusCode();
                        if (statusCode == 200) {
                            InputStream is = response.getEntity().getContent();
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            bitmaps.add(bitmap);
                        }
                    }
                    Message msg = Message.obtain();
                    msg.what = BITMAP_OK;
                    msg.obj = bitmaps;
                    mhandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    /**
     * 将图片保存到本地
     * @param path
     * @param bitmap
     */
    public void inputToOutput(String path, Bitmap bitmap) {
        int index = path.hashCode();
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(),
                    index + "");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取本地图片
     * @return
     */
    public List<Bitmap> getBitmaps() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            for (int i = 0; i < paths.size(); i++) {
                int index = paths.get(i).hashCode();
                Bitmap bitmap = null;
                File file = new File(Environment.getExternalStorageDirectory(),
                        index + "");
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                if (bitmap!=null){
                bitmaps.add(bitmap);
                }
            }
        }
        return bitmaps;
    }

    /**
     * viewpager的适配器
     */

    private PagerAdapter vp_adapter = new PagerAdapter() {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public int getCount() {

            return oImageViews.size();
        }

        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(oImageViews.get(position));
            return oImageViews.get(position);
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(oImageViews.get(position));
        }
    };

    private boolean isStop = false;
    private int index = 0;

    public void image_thread() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    while (!isStop) {
                        try {
                            sleep(2000);
                            index++;
                            mhandler.sendEmptyMessage(CHANGE_IMAGE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }
}
