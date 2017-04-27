package com.dp.test.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dp.test.R;
import com.dp.test.ui.view.PullToRefreshLayout;
import com.dp.test.util.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PullToRefreshLayoutActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_to_refresh_layout);


        final List<String> datas = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            datas.add("item - " + i);
        }

        final BaseAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                datas);
        // 获取listview实例
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        // 获取RefreshLayout实例
        final PullToRefreshLayout myRefreshListView = (PullToRefreshLayout) findViewById(R.id.activity_pull_to_refresh_layout);

        // 设置下拉刷新时的颜色值,颜色值需要定义在xml中
        myRefreshListView.setColorScheme(R.color.colorPrimary,
                        R.color.colorPrimaryDark,
                        R.color.colorAccent,
                        R.color.colorPrimary);
        // 设置下拉刷新监听器
        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                Toast.makeText(PullToRefreshLayoutActivity.this, "refresh", Toast.LENGTH_SHORT).show();

                myRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // 更新数据
                        datas.add(new Date().toGMTString());
                        adapter.notifyDataSetChanged();
                        // 更新完后调用该方法结束刷新
                        myRefreshListView.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        // 加载监听器
        myRefreshListView.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {

                ToastUtil.showTaost(PullToRefreshLayoutActivity.this, "onLoad");

                myRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        datas.add(new Date().toGMTString());
                        adapter.notifyDataSetChanged();
                        // 加载完后调用该方法
                        myRefreshListView.setLoading(false);
                    }
                }, 1500);

            }
        });

    }
}
