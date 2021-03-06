package net.sxkeji.androiddevartiestpritice.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import net.sxkeji.androiddevartiestpritice.R;
import net.sxkeji.androiddevartiestpritice.widget.Player;

public class VideoPlayerActivity extends Activity {
    private SurfaceView surfaceView;
    private RelativeLayout rlPlayWindow;
    private LinearLayout llProgress;
    private Button btnPlayVideo, btnBack;
    private SeekBar skbProgress;
    private Player player;
    private TextView tvTitle, tv_position, tv_duration;
    //    private String url = "http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4";
    private String url = "http://baobab.wdjcdn.com/1461595640215_6874_854x480.mp4";
    private boolean isShowControlWindow, isPlaying, isPause;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreenAndRemoveStatus();
        setContentView(R.layout.activity_video_player);

        initViews();
        setListeners();
    }

    /**
     * 设置全屏、隐藏状态栏
     */
    private void setFullScreenAndRemoveStatus() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void initViews() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tv_duration = (TextView) findViewById(R.id.tv_duration);
        tv_position = (TextView) findViewById(R.id.tv_position);
        btnPlayVideo = (Button) findViewById(R.id.btnPlayUrl);
        btnBack = (Button) findViewById(R.id.btn_back);
        rlPlayWindow = (RelativeLayout) findViewById(R.id.rl_play_window);
        llProgress = (LinearLayout) findViewById(R.id.ll_progress);

        skbProgress = (SeekBar) findViewById(R.id.skbProgress);
        skbProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());
        player = new Player(surfaceView, skbProgress, tv_position, tv_duration);

    }


    private void setListeners() {
        rlPlayWindow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissControlWindow(isShowControlWindow);
            }
        });

        btnPlayVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    if (isPause) {
                        player.play();
                        btnPlayVideo.setBackgroundResource(R.mipmap.icon_pause);
                        isPause = false;
                    } else {
                        player.pause();
                        btnPlayVideo.setBackgroundResource(R.mipmap.icon_play);
                        isPause = true;
                    }
                } else {
                    Toast.makeText(VideoPlayerActivity.this, "精彩马上就来，请稍等", Toast.LENGTH_SHORT).show();
                    player.playUrl(url);
                    isPlaying = true;
                    btnPlayVideo.setBackgroundResource(R.mipmap.icon_pause);
                    dismissControlWindow(true);
                }
            }
        });

    }

    /**
     * 隐藏控制界面
     *
     * @param show show if false, dismiss if true
     */
    private void dismissControlWindow(boolean show) {
        if (show) {
            btnPlayVideo.setVisibility(View.INVISIBLE);
            btnBack.setVisibility(View.INVISIBLE);
            tvTitle.setVisibility(View.INVISIBLE);
            llProgress.setVisibility(View.INVISIBLE);
            isShowControlWindow = false;
        } else {
            btnPlayVideo.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.VISIBLE);
            tvTitle.setVisibility(View.VISIBLE);
            llProgress.setVisibility(View.VISIBLE);
            isShowControlWindow = true;
        }
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()  
            this.progress = progress * player.mediaPlayer.getDuration()
                    / seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字  
            player.mediaPlayer.seekTo(progress);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
}