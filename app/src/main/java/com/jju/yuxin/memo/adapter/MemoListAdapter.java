package com.jju.yuxin.memo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jju.yuxin.memo.R;
import com.jju.yuxin.memo.bean.MemoBean;

import java.util.List;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.memo.adapter
 * Created by yuxin.
 * Created time 2016/11/2 0002 下午 8:13.
 * Version   1.0;
 * Describe : 列表的适配器
 * History:
 * ==============================================================================
 */

public class MemoListAdapter extends BaseAdapter {
    private List<MemoBean> memos;
    private Context context;
    private LayoutInflater inflater;

    public MemoListAdapter(Context context, List<MemoBean> memos) {
        this.context = context;
        this.memos = memos;
        inflater=LayoutInflater.from(context);
    }

    public void removeItme(int position){
        memos.remove(position);
    }
    @Override
    public int getCount() {
        return memos.size();
    }

    @Override
    public Object getItem(int position) {
        return memos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= inflater.inflate(R.layout.memo_item,null);
            viewHolder.item_title = (TextView) convertView.findViewById(R.id.item_title);
            viewHolder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.item_title.setText(memos.get(position).getBook_title());
        viewHolder.tv_author.setText(memos.get(position).getAuthor());
        viewHolder.tv_time.setText(memos.get(position).getBook_time());
        return convertView;
    }
    private  class ViewHolder{
        TextView item_title;
        TextView tv_author;
        TextView tv_time;
    }
}
