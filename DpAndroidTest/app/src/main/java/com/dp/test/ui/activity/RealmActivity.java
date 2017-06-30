package com.dp.test.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.dp.test.R;
import com.dp.test.bean.RealmTestBean;
import com.dp.test.debug.DpDebug;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmActivity extends AbstractActivity {

    private Realm myRealm;
    private int mCurGroupId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm);
        initRealm();
        writeRealm();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_read_all:
                readRealmAll();
                break;
            case R.id.btn_read_first_group:
                readFirstGroup();
                break;
            case R.id.btn_read_next_group:
                readNextGroup();
                break;
        }
    }

    private void initRealm() {
        myRealm = Realm.getInstance(
                new RealmConfiguration.Builder(this)
                        .name("myRealm.realm")
                        .build()
        );
    }

    private void writeRealm() {
        int i = 0;
        do {
            myRealm.beginTransaction();
            RealmTestBean myBean = myRealm.createObject(RealmTestBean.class);
            myBean.setTitle(String.valueOf(i));
            myBean.setGroudId(i / 5);
            myBean.setTime(System.currentTimeMillis());
            myRealm.commitTransaction();
        } while (++i < 20);
    }

    private void readRealmAll() {
        RealmResults<RealmTestBean> results = myRealm.where(RealmTestBean.class)
                .findAll();
        for (RealmTestBean bean : results) {
            DpDebug.log("RealmActivity ---- readRealmAll ---- title : " + bean.getTitle() + ", time : " + bean.getTime());
        }
    }

    private int getFirstGroupId() {
        RealmTestBean result = myRealm.where(RealmTestBean.class)
                .findFirst();
        if (result != null) {
            DpDebug.log("RealmActivity ---- readRealmFirst ---- title : " + result.getTitle() + ", time : " + result.getTime());
            return result.getGroudId();
        } else {
            DpDebug.log("RealmActivity ---- readRealmFirst ---- result is null");
            return 0;
        }
    }

    private int getNextGroupId() {
        RealmTestBean result = myRealm.where(RealmTestBean.class)
                .greaterThan("groudId", mCurGroupId)
                .findFirst();
        if (result != null) {
            DpDebug.log("RealmActivity ---- getNextGroupId ---- title : " + result.getTitle() + ", time : " + result.getTime());
            return result.getGroudId();
        } else {
            DpDebug.log("RealmActivity ---- getNextGroupId ---- result is null");
            return 0;
        }
    }

    private void readFirstGroup() {
        mCurGroupId = getFirstGroupId();
        RealmResults<RealmTestBean> results = myRealm.where(RealmTestBean.class)
                .equalTo("groudId", mCurGroupId)
                .findAll();
        for (RealmTestBean bean : results) {
            DpDebug.log("RealmActivity ---- readFirstGroup ---- mCurGroupId : " + mCurGroupId + ", title : " + bean.getTitle() + ", time : " + bean.getTime());
        }
    }

    private void readNextGroup() {
        mCurGroupId = getNextGroupId();
        if (mCurGroupId == 0) {
            return;
        }
        RealmResults<RealmTestBean> results = myRealm.where(RealmTestBean.class)
                .equalTo("groudId", mCurGroupId)
                .findAll();
        for (RealmTestBean bean : results) {
            DpDebug.log("RealmActivity ---- readNextGroup ---- mCurGroupId : " + mCurGroupId + ", title : " + bean.getTitle() + ", time : " + bean.getTime());
        }
    }

}
