package com.dp.test.bean;

import io.realm.RealmObject;

/**
 * Created by dapau on 2017/6/30.
 */

public class RealmTestBean extends RealmObject {
    private String title;
    private int groudId;
    private long time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGroudId() {
        return groudId;
    }

    public void setGroudId(int groudId) {
        this.groudId = groudId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
