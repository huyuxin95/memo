package com.jju.yuxin.memo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * =============================================================================
 * Copyright (c) 2016 yuxin All rights reserved.
 * Packname com.jju.yuxin.memo.bean
 * Created by yuxin.
 * Created time 2016/11/2 0002 下午 4:32.
 * Version   1.0;
 * Describe : 用户bean
 * History:
 * ==============================================================================
 */

public class UserBean implements Parcelable {
    private int id;
    private String name;
    private String psd;
    private String todo;

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public UserBean() {
    }

    public UserBean(String name, String psd) {
        this.name = name;
        this.psd = psd;
    }

    public UserBean(String name, String psd, String todo) {
        this.name = name;
        this.psd = psd;
        this.todo = todo;
    }

    public UserBean(int id, String name, String psd) {
        this.id = id;
        this.name = name;
        this.psd = psd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPsd() {
        return psd;
    }

    public void setPsd(String psd) {
        this.psd = psd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.psd);
        dest.writeString(this.todo);
    }

    protected UserBean(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.psd = in.readString();
        this.todo = in.readString();
    }

    public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", psd='" + psd + '\'' +
                ", todo='" + todo + '\'' +
                '}';
    }
}
