package com.dp.test.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.dp.test.R;
import com.dp.test.bean.NestedMainListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dapau on 2017/6/28.
 */

public class NestedMainListView extends ListView {

    private Context mContext;
    private List<NestedMainListBean> mMainList = new ArrayList<>();
    private MyAdapter mAdapter;

    public NestedMainListView(Context context) {
        this(context, null);
    }

    public NestedMainListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedMainListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void init(List<NestedMainListBean> mainList) {
        mMainList = mainList;
        mAdapter = new MyAdapter(mContext);
        setAdapter(mAdapter);
    }

    private class ViewHolder {
        public NestedSubListView nestedSubListView;
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mMainList.size();
        }

        @Override
        public Object getItem(int position) {
            return mMainList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final NestedMainListBean bean = mMainList.get(position);
            ViewHolder tempViewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_nested_main, null);

                tempViewHolder = new ViewHolder();
                tempViewHolder.nestedSubListView = (NestedSubListView) convertView.findViewById(R.id.nested_sub_list_view);

                convertView.setTag(tempViewHolder);
            } else {
                tempViewHolder = (ViewHolder) convertView.getTag();
            }

            tempViewHolder.nestedSubListView.init(bean);

            return convertView;
        }

        public void refresh() {
            notifyDataSetChanged();
        }
    }
}
