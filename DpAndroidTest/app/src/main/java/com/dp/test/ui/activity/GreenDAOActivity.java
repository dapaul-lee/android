package com.dp.test.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dp.test.R;
import com.dp.test.db.DpNote;
import com.dp.test.db.DpNoteDao;
import com.dp.test.debug.DpDebug;
import com.dp.test.helper.GreenDAOHelper;
import com.dp.test.util.ToastUtil;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

public class GreenDAOActivity extends AbstractActivity {

    private ListView mListView;
    private EditText mNote;

    private DpNoteDao mDpNoteDao;

    private ListViewAdapter mListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_dao);
        mListView = (ListView) findViewById(R.id.list);
        mNote = (EditText) findViewById(R.id.note_add);

        mDpNoteDao = GreenDAOHelper.getDaoSession().getDpNoteDao();

        mListViewAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mListViewAdapter);
        queryNotes();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                addNote();
                break;
            case R.id.btn_delete:
//                switchToActivity(RxJavaActivity.class);
                break;
            case R.id.btn_modify:
//                switchToActivity(GreenDAOActivity.class);
                break;
            case R.id.btn_query:
                queryNotes();
//                switchToActivity(GreenDAOActivity.class);
                break;
        }
    }

    private void addNote() {
        String note = mNote.getText().toString();
        if (note == null || note.isEmpty()) {
            ToastUtil.showTaost(this, "Please add your note.");
            return;
        }
        DpNote dpNote = new DpNote();
        dpNote.setText(note);
        mDpNoteDao.insert(dpNote);
        mNote.setText("");
    }

    private void queryNotes() {
        List<DpNote> noteList = mDpNoteDao.queryBuilder().orderAsc(DpNoteDao.Properties.Text).build().list();
        DpDebug.log("GreenDAOActivity ---- queryNotes ---- noteList : " + noteList.size());
        mListViewAdapter.setDpNoteList(noteList);
//        mListViewAdapter.notifyDataSetChanged();
    }

    private void deleteNote(){

    }

    public class ListViewAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public ListViewAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        private List<DpNote> mDpNoteList = new ArrayList<>();

        public void setDpNoteList(List<DpNote> dpNoteList) {
            mDpNoteList = dpNoteList;
        }

        @Override
        public int getCount() {
            return mDpNoteList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDpNoteList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final DpNote note = mDpNoteList.get(position);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_green_dao_note, null);
                viewHolder = new ViewHolder();
                viewHolder.tvId = (TextView) convertView.findViewById(R.id.tv_id);
                viewHolder.tvNote = (TextView) convertView.findViewById(R.id.tv_note);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tvId.setText(note.getId().toString());
            viewHolder.tvNote.setText(note.getText());
            return convertView;
        }

//        public void refresh() {
//            notifyDataSetChanged();
//        }
    }

    private class ViewHolder {
        public TextView tvId;
        public TextView tvNote;
    }

}
