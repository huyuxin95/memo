package com.jju.yuxin.memo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.memo.bean
 * Created by yuxin.
 * Created time 2016/11/2 0002 下午 7:46.
 * Version   1.0;
 * Describe : 备忘录的Bean
 * History:
 * ==============================================================================
 */

public class MemoBean implements Parcelable {

    private int id;
    private String author;
    private String book_content;
    private String book_title;
    private String book_time;

    public MemoBean() {
    }

    public MemoBean(int id, String book_title, String book_content, String book_time, String author) {
        this.id = id;
        this.book_title = book_title;
        this.book_content = book_content;
        this.book_time = book_time;
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBook_content() {
        return book_content;
    }

    public void setBook_content(String book_content) {
        this.book_content = book_content;
    }

    public String getBook_time() {
        return book_time;
    }

    public void setBook_time(String book_time) {
        this.book_time = book_time;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MemoBean{" +
                "author='" + author + '\'' +
                ", id=" + id +
                ", book_content='" + book_content + '\'' +
                ", book_title='" + book_title + '\'' +
                ", book_time='" + book_time + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.author);
        dest.writeString(this.book_content);
        dest.writeString(this.book_title);
        dest.writeString(this.book_time);
    }

    protected MemoBean(Parcel in) {
        this.id = in.readInt();
        this.author = in.readString();
        this.book_content = in.readString();
        this.book_title = in.readString();
        this.book_time = in.readString();
    }

    public static final Parcelable.Creator<MemoBean> CREATOR = new Parcelable.Creator<MemoBean>() {
        @Override
        public MemoBean createFromParcel(Parcel source) {
            return new MemoBean(source);
        }

        @Override
        public MemoBean[] newArray(int size) {
            return new MemoBean[size];
        }
    };
}
