package com.dp.test.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dp.test.R;
import com.dp.test.app.DpApplication;
import com.dp.test.bean.AppInfo;
import com.dp.test.bean.AppInfoRich;
import com.dp.test.debug.DpDebug;
import com.dp.test.util.BitmapUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RxJavaActivity extends AbstractActivity {

    private Context mContext;
    private File mFilesDir;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private ApplicationAdapter mAdapter;

    private List<AppInfo> mAppList = new ArrayList<AppInfo>();

    Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(ObservableEmitter<Integer> e) throws Exception {
            for (int i = 0; i < 5; i++) {
                e.onNext(i);
            }
            e.onComplete();
        }
    });

    Observable observableEmpty = Observable.empty();

    Observer<Integer> observer = new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Integer value) {
            DpDebug.log("RxJavaActivity ---- observer ---- onNext ---- value : " + value);
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {
            DpDebug.log("RxJavaActivity ---- observer ---- onComplete");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mFilesDir = DpApplication.getInstance().getFilesDir();
        DpDebug.log("RxJavaActivity ---- onCreate ---- mFilesDir : " + mFilesDir.toString());
        setContentView(R.layout.activity_rx_java);

//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mRecyclerView = (RecyclerView) findViewById(R.id.app_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ApplicationAdapter(mAppList, R.layout.applications_list_item);
        mRecyclerView.setAdapter(mAdapter);
//        mSwipeRefreshLayout.setRefreshing(false);

        rxJavaRun();
    }

    private void rxJavaRun() {
//        observable.subscribe(observer);
//        observableEmpty.subscribe(observer);
        getApps()
//                .toSortedList()
                .subscribe(new Observer<AppInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AppInfo value) {
                        DpDebug.log("RxJavaActivity ---- onNext ---- appInfo : " + value.getName());
//                        mAppList.add(value);
//                        mAdapter.refresh();
                        mAdapter.addApplication(value);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        DpDebug.log("RxJavaActivity ---- onComplete");
                    }
                });
    }

//    private Observable<AppInfo> getApps() {
//        return Observable.create(new Action1<AppInfo>() {});
//    }

    private Observable<AppInfo> getApps() {
        return Observable.create(new ObservableOnSubscribe<AppInfo>() {
            @Override
            public void subscribe(ObservableEmitter<AppInfo> subscriber) throws Exception {
                List<AppInfoRich> apps = new ArrayList<AppInfoRich>();
                final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> infos = mContext.getPackageManager().queryIntentActivities(mainIntent, 0);

                for (ResolveInfo info : infos) {
                    apps.add(new AppInfoRich(mContext, info));
                }

                for (AppInfoRich appInfo : apps) {
                    Bitmap icon = BitmapUtil.drawableToBitmap(appInfo.getIcon());
                    String name = appInfo.getName();
                    String iconPath = mFilesDir + "/" + name;
                    BitmapUtil.storeBitmap(mContext, icon, name);

                    subscriber.onNext(new AppInfo(name, iconPath, appInfo.getLastUpdateTime()));
                }
            }
        });
    }


    public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

        private List<AppInfo> mApplications;

        private int mRowLayout;

        public ApplicationAdapter(List<AppInfo> applications, int rowLayout) {
            mApplications = applications;
            mRowLayout = rowLayout;
        }

        public void addApplications(List<AppInfo> applications) {
            mApplications.clear();
            mApplications.addAll(applications);
            notifyDataSetChanged();
        }

        public void addApplication(int position, AppInfo appInfo) {
            if (position < 0) {
                position = 0;
            }
            mApplications.add(position, appInfo);
            notifyItemInserted(position);
        }

        public void addApplication(AppInfo appInfo) {
            mApplications.add(appInfo);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(mRowLayout, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            final AppInfo appInfo = mApplications.get(i);
            viewHolder.name.setText(appInfo.getName());
            viewHolder.image.setImageBitmap(BitmapFactory.decodeFile(appInfo.getIcon()));
//            DpDebug.log("RxJavaActivity ---- onBindViewHolder ---- appInfo.getIcon() : " + appInfo.getIcon());
//            getBitmap(appInfo.getIcon())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<Bitmap>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(Bitmap value) {
//                            DpDebug.log("RxJavaActivity ---- onBindViewHolder ---- onNext ---- (value != null) : " + (value != null));
//                            viewHolder.image.setImageBitmap(value);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
//                    .subscribe(viewHolder.image::setImageBitmap);
        }

        @Override
        public int getItemCount() {
            return mApplications == null ? 0 : mApplications.size();
        }

        private Observable<Bitmap> getBitmap(final String icon) {
            return Observable.create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                    e.onNext(BitmapFactory.decodeFile(icon));
                    e.onComplete();
                }
            });
//            subscriber -> {
//                subscriber.onNext(BitmapFactory.decodeFile(icon));
//                subscriber.onCompleted();
//            });
        }

        public void refresh() {
            notifyDataSetChanged();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView name;

            public ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                image = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    }

}
