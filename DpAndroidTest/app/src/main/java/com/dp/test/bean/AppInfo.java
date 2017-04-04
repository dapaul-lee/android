package com.dp.test.bean;

import java.util.Comparator;

/**
 * Created by dapaul on 2017/4/1.
 */

public class AppInfo implements Comparable<Object> {

    long lastUpdateTime;
    String name;
    String icon;

    public AppInfo(String name, String icon, long lastUpdateTime) {
        this.name = name;
        this.icon = icon;
        this.lastUpdateTime = lastUpdateTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public int compareTo(Object another) {
        AppInfo appInfo = (AppInfo)another;
        return getName().compareTo(appInfo.getName());
    }
}
