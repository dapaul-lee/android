package com.dp.test.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dapau on 2017/6/28.
 */

public class NestedMainListBean {
    private int date;
    private List<NestedSubListBean> subList = new ArrayList<>();

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public List<NestedSubListBean> getSubList() {
        return subList;
    }

    public void setSubList(List<NestedSubListBean> subList) {
        this.subList = subList;
    }
}
