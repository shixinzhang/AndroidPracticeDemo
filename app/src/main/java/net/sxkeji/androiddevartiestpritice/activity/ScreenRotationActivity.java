package net.sxkeji.androiddevartiestpritice.activity;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.widget.TextView;

import net.sxkeji.androiddevartiestpritice.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 屏幕旋转监听 <br/>
 * 别忘了开启手机的 自动旋转
 * Created by zhangshixin on 5/14/2016.
 */
public class ScreenRotationActivity extends Activity{
    private final String TAG = "ScreenRotationActivity";
    @Bind(R.id.tv_rotation)
    TextView tvRotation;
    private OrientationEventListener mOrientationListener;
    private boolean mScreenProtrait = true;
    private boolean mCurrentOrient = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        setContentView(R.layout.activity_screen_rotation);
        ButterKnife.bind(this);
        startOrientationChangeListener();
    }

    /**
     * 方法一：监听配置环境变化
     */
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        Log.e(TAG, "onConfigurationChanged");
//        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        int rotation = manager.getDefaultDisplay().getRotation();
//        tvRotation.setText("rotation " + rotation * 90);
//    }

    /**
     * 方法二：开启方向监听器
     */
    private final void startOrientationChangeListener() {
        mOrientationListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int rotation) {
                if (((rotation >= 0) && (rotation <= 45)) || (rotation >= 315) || ((rotation >= 135) && (rotation <= 225))) {//portrait
                    mCurrentOrient = true;
                    if (mCurrentOrient != mScreenProtrait) {
                        mScreenProtrait = mCurrentOrient;
                        Log.d(TAG, "Screen orientation changed from Landscape to Portrait!");
                    }
                } else if (((rotation > 45) && (rotation < 135)) || ((rotation > 225) && (rotation < 315))) {//landscape
                    mCurrentOrient = false;
                    if (mCurrentOrient != mScreenProtrait) {
                        mScreenProtrait = mCurrentOrient;
                        Log.d(TAG, "Screen orientation changed from Portrait to Landscape!");
                    }
                }
                tvRotation.setText("rotation degree :" + rotation);
            }
        };
        mOrientationListener.enable();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOrientationListener.disable();
    }
}

