package com.dp.test.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.dp.test.R;
import com.dp.test.config.VideoConstant;
import com.squareup.picasso.Picasso;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static android.support.v7.recyclerview.R.styleable.RecyclerView;

public class JieCaoVideoPlayerActivity extends AbstractActivity {

    RecyclerView recyclerView;
    RecyclerViewVideoAdapter adapterVideoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("NormalRecyclerView");
        setContentView(R.layout.activity_jie_cao_video_player);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapterVideoList = new RecyclerViewVideoAdapter(this);
        recyclerView.setAdapter(adapterVideoList);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    * Class : RecyclerViewVideoAdapter
    * */
    public class RecyclerViewVideoAdapter extends RecyclerView.Adapter<RecyclerViewVideoAdapter.MyViewHolder> {

        int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        private Context context;
        public static final String TAG = "VideoAdapter";

        public RecyclerViewVideoAdapter(Context context) {
            this.context = context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.list_item_jiecao_videoview, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Log.i(TAG, "onBindViewHolder [" + holder.jcVideoPlayer.hashCode() + "] position=" + position);

            holder.jcVideoPlayer.setUp(
                    VideoConstant.videoUrls[0][position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                    VideoConstant.videoTitles[0][position]);
            Picasso.with(holder.jcVideoPlayer.getContext())
                    .load(VideoConstant.videoThumbs[0][position])
                    .into(holder.jcVideoPlayer.thumbImageView);
        }

        @Override
        public int getItemCount() {
            return videoIndexs.length;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            JCVideoPlayerStandard jcVideoPlayer;

            public MyViewHolder(View itemView) {
                super(itemView);
                jcVideoPlayer = (JCVideoPlayerStandard) itemView.findViewById(R.id.videoplayer);
            }
        }

    }
}
