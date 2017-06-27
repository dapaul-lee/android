package com.dp.test.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dp.test.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dapau on 2017/6/27.
 */

public class DragToSortRecycleView extends RecyclerView {

    public interface OnItemCallbackListener {
        /**
         * @param fromPosition 起始位置
         * @param toPosition   移动的位置
         */
        void onMove(int fromPosition, int toPosition);

        void onSwipe(int position);
    }

    private Context mContext;

    public DragToSortRecycleView(Context context) {
        this(context, null);
    }

    public DragToSortRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragToSortRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        final MyAdapter adapter = new MyAdapter(mContext);
        setAdapter(adapter);

        ItemTouchHelper.Callback callback = new OnItemCallbackHelper(adapter);

        /**
         * 实例化ItemTouchHelper对象,然后添加到RecyclerView
         */
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(this);
    }

    public class OnItemCallbackHelper extends ItemTouchHelper.Callback {

        private MyAdapter myAdapter;

        public OnItemCallbackHelper(MyAdapter adapter) {
            myAdapter = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
            int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.RIGHT;
            int swipeFlag = ItemTouchHelper.START | ItemTouchHelper.END;

            return makeMovementFlags(dragFlag, swipeFlag);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
            myAdapter.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(ViewHolder viewHolder, int direction) {
            myAdapter.onSwipe(viewHolder.getAdapterPosition());
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> implements OnItemCallbackListener {

        private List<String> mData;
        private Context mContext;

        public MyAdapter(Context mContext) {
            this.mContext = mContext;
            mData = new ArrayList<>();
            mData.add("one");
            mData.add("two");
            mData.add("three");
            mData.add("four");
            mData.add("five");
            mData.add("six");
            mData.add("seven");
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.recycleview_item_drag_to_sort, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(final Holder holder, int position) {
            holder.tv.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public void onMove(int fromPosition, int toPosition) {
            /**
             * 在这里进行给原数组数据的移动
             */
            Collections.swap(mData, fromPosition, toPosition);
            /**
             * 通知数据移动
             */
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onSwipe(int position) {
            /**
             * 原数据移除数据
             */
            mData.remove(position);
            /**
             * 通知移除
             */
            notifyItemRemoved(position);
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tv;

            public Holder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }
}
