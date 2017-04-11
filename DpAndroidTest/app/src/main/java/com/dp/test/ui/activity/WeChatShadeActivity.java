package com.dp.test.ui.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dp.test.R;

public class WeChatShadeActivity extends AbstractActivity implements SensorEventListener {

    private static final int SHADE_SENSITIVITY = 15;

    private static final int SHAKE_START = 1;
    private static final int SHAKE_SECOND = 2;
    private static final int SHAKE_END = 3;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Vibrator mVibrator;
    private SoundPool mSoundPool;

    private boolean isShake = false;

    private LinearLayout mTopLayout;
    private LinearLayout mBottomLayout;
    private ImageView mTopLine;
    private ImageView mBottomLine;

    private int mWeiChatAudio;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHAKE_START:
                    mVibrator.vibrate(300);
                    mSoundPool.play(mWeiChatAudio, 1, 1, 0, 0, 1);
                    mTopLine.setVisibility(View.VISIBLE);
                    mBottomLine.setVisibility(View.VISIBLE);
                    startAnimation(false);
                    break;
                case SHAKE_SECOND:
                    mVibrator.vibrate(300);
                    break;
                case SHAKE_END:
                    isShake = false;
                    startAnimation(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chat_shade);
        initView();
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        mWeiChatAudio = mSoundPool.load(this, R.raw.weichat_audio, 1);
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    protected void onPause() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        if (mSensorManager != null) {
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mAccelerometerSensor != null) {
                mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    private void initView() {
        mTopLayout = (LinearLayout) findViewById(R.id.main_linear_top);
        mBottomLayout = ((LinearLayout) findViewById(R.id.main_linear_bottom));
        mTopLine = (ImageView) findViewById(R.id.main_shake_top_line);
        mBottomLine = (ImageView) findViewById(R.id.main_shake_bottom_line);
        mTopLine.setVisibility(View.GONE);
        mBottomLine.setVisibility(View.GONE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int type = event.sensor.getType();

        if (type == Sensor.TYPE_ACCELEROMETER) {
            float[] values = event.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];

            if ((Math.abs(x) > SHADE_SENSITIVITY || Math.abs(y) > SHADE_SENSITIVITY || Math
                    .abs(z) > SHADE_SENSITIVITY) && !isShake) {
                isShake = true;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            mHandler.obtainMessage(SHAKE_START).sendToTarget();
                            Thread.sleep(500);
                            mHandler.obtainMessage(SHAKE_SECOND).sendToTarget();
                            Thread.sleep(500);
                            mHandler.obtainMessage(SHAKE_END).sendToTarget();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void startAnimation(boolean isBack) {
        int type = Animation.RELATIVE_TO_SELF;
        float topFromY;
        float topToY;
        float bottomFromY;
        float bottomToY;
        if (isBack) {
            topFromY = -0.5f;
            topToY = 0;
            bottomFromY = 0.5f;
            bottomToY = 0;
        } else {
            topFromY = 0;
            topToY = -0.5f;
            bottomFromY = 0;
            bottomToY = 0.5f;
        }

        TranslateAnimation topAnim = new TranslateAnimation(
                type, 0, type, 0, type, topFromY, type, topToY
        );
        topAnim.setDuration(200);
        topAnim.setFillAfter(true);

        TranslateAnimation bottomAnim = new TranslateAnimation(
                type, 0, type, 0, type, bottomFromY, type, bottomToY
        );
        bottomAnim.setDuration(200);
        bottomAnim.setFillAfter(true);

        if (isBack) {
            bottomAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mTopLine.setVisibility(View.GONE);
                    mBottomLine.setVisibility(View.GONE);
                }
            });
        }
        mTopLayout.startAnimation(topAnim);
        mBottomLayout.startAnimation(bottomAnim);
    }
}
