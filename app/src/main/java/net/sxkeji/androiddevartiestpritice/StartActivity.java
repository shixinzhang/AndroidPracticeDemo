package net.sxkeji.androiddevartiestpritice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import net.sxkeji.androiddevartiestpritice.activity.GankListActivity;
import net.sxkeji.androiddevartiestpritice.activity.IPCActivity;
import net.sxkeji.androiddevartiestpritice.activity.ListViewLoadDataActivity;
import net.sxkeji.androiddevartiestpritice.activity.RecyclerViewLoadDataActivity;
import net.sxkeji.androiddevartiestpritice.activity.RemoteViewActivity;
import net.sxkeji.androiddevartiestpritice.activity.ScreenRotationActivity;
import net.sxkeji.androiddevartiestpritice.activity.ScrollerLearningActivity;
import net.sxkeji.androiddevartiestpritice.activity.StartLockScreenActivity;
import net.sxkeji.androiddevartiestpritice.activity.TimePickerParseActivity;
import net.sxkeji.androiddevartiestpritice.activity.VerticalSelectTabActivity;
import net.sxkeji.androiddevartiestpritice.activity.VideoPlayerActivity;
import net.sxkeji.androiddevartiestpritice.activity.WidgetCircleViewActivity;
import net.sxkeji.androiddevartiestpritice.activity.learnInterpolator.AnimationSplashActivity;
import net.sxkeji.androiddevartiestpritice.activity.eventbus.EventBusActivity;
import net.sxkeji.androiddevartiestpritice.activity.sidebarlist.SideBarListActivity;
import net.sxkeji.androiddevartiestpritice.widget.JWordUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主页
 * Created by zhangshixin on 4/9/2016.
 */
public class StartActivity extends Activity {
    @Bind(R.id.ll_content)
    LinearLayout llContent;
    @Bind(R.id.btn_parse_timepicker)
    Button btnParseTimepicker;
    @Bind(R.id.btn_recycler_load_data)
    Button btnRecyclerLoadData;
    @Bind(R.id.btn_widget_circle_view)
    Button btnWidgetCircleView;
    @Bind(R.id.btn_video_play)
    Button btnVideoPlay;
    @Bind(R.id.btn_start_lock)
    Button btnStartLock;
    @Bind(R.id.btn_generate_doc)
    Button btnGenerateDoc;
    @Bind(R.id.btn_gank_list)
    Button btnGankList;
    @Bind(R.id.btn_screen_rotation)
    Button btnScreenRotation;
    @Bind(R.id.btn_scroller_learning)
    Button btnScrollerLearning;
    @Bind(R.id.btn_eventbus)
    Button btnEventBus;
    @Bind(R.id.btn_drawer_sort_list)
    Button btnDrawerSortList;
    @Bind(R.id.btn_animation_splash)
    Button btnAnimationSplash;

    private Button btn_ipc;
    private Button btn_listview_load_data;
    private Button btn_vertical_select;
    private Button btn_remote_views;

    /**
     * RemoteView更新UI接收器
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            RemoteViews remoteViews = intent.getParcelableExtra(Constants.EXTRA_REMOTE_VIEWS);
            if (remoteViews != null) {
                updateUI(remoteViews);
            }
        }
    };

    private void updateUI(RemoteViews remoteViews) {
        //RemoteViews 的 apply和reapply的区别在于: apply多了一步:布局的解析、加载
//        View view = remoteViews.apply(this, llContent);
        int layoutId = getResources().getIdentifier("remoteview_simulate_notification", "layout", getPackageName());
        View view = getLayoutInflater().inflate(layoutId, llContent, false);
        remoteViews.reapply(this, view);

        llContent.addView(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        initView();
        setListener();
    }

    private void setListener() {
        btn_ipc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(IPCActivity.class);
            }
        });
        btn_listview_load_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(ListViewLoadDataActivity.class);
            }
        });

        btnRecyclerLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(RecyclerViewLoadDataActivity.class);
            }
        });
        btn_vertical_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(VerticalSelectTabActivity.class);
            }
        });
        btn_remote_views.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(RemoteViewActivity.class);
            }
        });
        btnParseTimepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(TimePickerParseActivity.class);
            }
        });
        btnWidgetCircleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(WidgetCircleViewActivity.class);
            }
        });
        btnVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                JCFullScreenActivity.toActivity(StartActivity.this,
//                        "http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4",
//                        JCVideoPlayerStandard.class,
//                        "sss");
                jump2Activity(VideoPlayerActivity.class);
            }
        });

        btnStartLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(StartLockScreenActivity.class);
                finish();
            }
        });

        btnGenerateDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JWordUtils.create(StartActivity.this, "/sdcard/365_diary/zsxword2.docx");
            }
        });

        btnGankList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(GankListActivity.class);
            }
        });
        btnScreenRotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(ScreenRotationActivity.class);
            }
        });
        btnScrollerLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(ScrollerLearningActivity.class);
            }
        });
        btnEventBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(EventBusActivity.class);
            }
        });
        btnDrawerSortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(SideBarListActivity.class);
            }
        });
        btnAnimationSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(AnimationSplashActivity.class);
            }
        });

    }

    private void jump2Activity(Class<?> aClass) {
        startActivity(new Intent(StartActivity.this, aClass));
    }

    private void initView() {
        btn_ipc = (Button) findViewById(R.id.btn_ipc);
        btn_listview_load_data = (Button) findViewById(R.id.btn_listview_load_data);
        btn_vertical_select = (Button) findViewById(R.id.btn_vertical_select);
        btn_remote_views = (Button) findViewById(R.id.btn_remote_views);

        IntentFilter intentFilter = new IntentFilter(Constants.SIMULATE_NOTIFICATION_ACTION);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }
}
