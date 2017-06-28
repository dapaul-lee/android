package com.dp.test.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dp.test.R;
import com.dp.test.bean.NestedMainListBean;
import com.dp.test.bean.NestedSubListBean;

/**
 * Created by dapau on 2017/6/28.
 */

public class NestedSubListView extends ListView {

    private Context mContext;
    private NestedMainListBean mMainBean;
    private MyAdapter mAdapter;

    public NestedSubListView(Context context) {
        this(context, null);
    }

    public NestedSubListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedSubListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }

    public void init(NestedMainListBean mainBean) {
        mMainBean = mainBean;
        initHeaderView();
        mAdapter = new MyAdapter(mContext);
        setAdapter(mAdapter);
    }

    private void initHeaderView() {
        LayoutInflater lif = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View headerView = lif.inflate(R.layout.header_nested_sub, null);
        TextView headerText = (TextView) headerView.findViewById(R.id.header_text);
        headerText.setText(String.valueOf(mMainBean.getDate()));
        addHeaderView(headerView);
    }

    private class ViewHolder {
        public TextView textView;
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mMainBean.getSubList().size();
        }

        @Override
        public Object getItem(int position) {
            return mMainBean.getSubList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final NestedSubListBean bean = mMainBean.getSubList().get(position);
            ViewHolder tempViewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_nested_sub, null);

                tempViewHolder = new ViewHolder();
                tempViewHolder.textView = (TextView) convertView.findViewById(R.id.tv);

                convertView.setTag(tempViewHolder);
            } else {
                tempViewHolder = (ViewHolder) convertView.getTag();
            }

            tempViewHolder.textView.setText(bean.getContent());

            return convertView;
        }

        public void refresh() {
            notifyDataSetChanged();
        }
    }
}
