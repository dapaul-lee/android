package com.dp.test.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.dp.test.R;
import com.dp.test.debug.DpDebug;
import com.dp.test.helper.ImageLoader;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;
import com.sromku.simple.storage.helpers.OrderType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
* Some blog that is helpful:
* http://blog.csdn.net/smallcheric/article/details/51055013
* http://blog.csdn.net/smallcheric/article/details/51055095
* */

public class PicassoActivity extends AbstractActivity {

    private ListView mListView;
    private ImageListAdapter mAdapter;

    private Storage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picasso);

        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new ImageListAdapter(this);
        mListView.setAdapter(mAdapter);

        initStorage();
    }

    private void initStorage() {
        mStorage = SimpleStorage.getExternalStorage(Environment.DIRECTORY_PICTURES);

        List<File> files = mStorage.getFiles("xinlanedit", OrderType.DATE);
        DpDebug.log("PicassoActivity ---- initStorage ---- files : " + files.size());
        mAdapter.setFileList(files);
    }

    private class ViewHolder {
        public ImageView iv;
    }

    private class ImageListAdapter extends BaseAdapter {
        private Context context;

        private List<File> fileList = new ArrayList<>();

        public ImageListAdapter(Context context) {
            super();

            this.context = context;

        }

        private void setFileList(List<File> files) {
            fileList = files;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return fileList.size();
        }

        @Override
        public Object getItem(int position) {
            return fileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.list_item_picasso, null);
                viewHolder = new ViewHolder();
                viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //加载图片
            ImageLoader.loadImageByFile(context, viewHolder.iv, fileList.get(position));

            return convertView;
        }
    }
}
