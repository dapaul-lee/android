package com.dp.test.ui.activity;

import android.os.Bundle;

import com.dp.test.R;
import com.dp.test.bean.NestedMainListBean;
import com.dp.test.bean.NestedSubListBean;
import com.dp.test.ui.view.NestedMainListView;

import java.util.ArrayList;
import java.util.List;

public class NestedListViewActivity extends AbstractActivity {

    private NestedMainListView mNestedMainListView;
    private List<NestedMainListBean> mMainBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_list_view);

        mNestedMainListView = (NestedMainListView) findViewById(R.id.nested_main_list_view);
        testNestedListView();
    }

    private void testNestedListView() {
        mMainBeanList = new ArrayList<>();

        NestedMainListBean mainBean = new NestedMainListBean();
        mainBean.setDate(1);
        NestedSubListBean subBean = new NestedSubListBean();
        subBean.setContent("android");
        mainBean.getSubList().add(subBean);

        subBean = new NestedSubListBean();
        subBean.setContent("ios");
        mainBean.getSubList().add(subBean);

        mMainBeanList.add(mainBean);

        mainBean = new NestedMainListBean();
        mainBean.setDate(2);
        subBean = new NestedSubListBean();
        subBean.setContent("java");
        mainBean.getSubList().add(subBean);

        subBean = new NestedSubListBean();
        subBean.setContent("lisp");
        mainBean.getSubList().add(subBean);

        subBean = new NestedSubListBean();
        subBean.setContent("csharp");
        mainBean.getSubList().add(subBean);

        mMainBeanList.add(mainBean);

        mainBean = new NestedMainListBean();
        mainBean.setDate(3);
        subBean = new NestedSubListBean();
        subBean.setContent("kotlin");
        mainBean.getSubList().add(subBean);

        subBean = new NestedSubListBean();
        subBean.setContent("swift");
        mainBean.getSubList().add(subBean);

        subBean = new NestedSubListBean();
        subBean.setContent("python");
        mainBean.getSubList().add(subBean);

        mMainBeanList.add(mainBean);


        mNestedMainListView.init(mMainBeanList);

    }
}
