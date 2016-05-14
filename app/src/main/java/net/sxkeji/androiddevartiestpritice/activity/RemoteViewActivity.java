package net.sxkeji.androiddevartiestpritice.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import net.sxkeji.androiddevartiestpritice.Constants;
import net.sxkeji.androiddevartiestpritice.R;
import net.sxkeji.androiddevartiestpritice.StartActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * RemoteViews学习
 * Created by zhangshixin on 4/20/2016.
 */
public class RemoteViewActivity extends Activity {
    @Bind(R.id.btn_notification_default)
    Button btnNotificationDefault;
    @Bind(R.id.btn_notification_diy)
    Button btnNotificationDiy;
    @Bind(R.id.btn_simulate_notification)
    Button btnSimulateNotification;

    private int clickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_view);
        ButterKnife.bind(this);

        setListeners();
    }

    private void setListeners() {
        btnNotificationDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDefaultNotification();
            }
        });
        btnNotificationDiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDIYNotification();
            }
        });
        btnSimulateNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimulateNotification();
            }
        });
    }

    /**
     * 发送模拟通知栏消息
     */
    private void showSimulateNotification() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.remoteview_simulate_notification);
        remoteViews.setTextViewText(R.id.tv_title, "msg from process :" + android.os.Process.myPid());
        remoteViews.setImageViewResource(R.id.iv_img, R.mipmap.icon_ez);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, RemoteViewActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.rl_simulate_notification, pendingIntent);

        Intent intent = new Intent(Constants.SIMULATE_NOTIFICATION_ACTION);
        intent.putExtra(Constants.EXTRA_REMOTE_VIEWS, remoteViews);
        sendBroadcast(intent);
        Toast.makeText(this,"通知栏消息发送成功",Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 显示自定义通知栏消息
     */
    private void showDIYNotification() {
        Intent intent = new Intent(RemoteViewActivity.this, StartActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.remoteview_notification);
        remoteViews.setTextViewText(R.id.tv_notify_title, "自定义标题");
        remoteViews.setTextViewText(R.id.tv_notify_content, "点我进入城市选择");
        remoteViews.setImageViewResource(R.id.iv_notify_img, R.mipmap.ic_launcher);

        PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0,
                new Intent(this, VerticalSelectTabActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.tv_notify_content, pendingIntent1);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder(this)
                .setContent(remoteViews)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("您有新的自定义消息")
                .getNotification();

        notify.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(clickTime++, notify);
    }

    private void showDefaultNotification() {
        Intent intent = new Intent(RemoteViewActivity.this, StartActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("您有新的消息请注意查收")
                .setContentText("zhangshixin is so handsome " + clickTime)
                .setContentTitle("真相就是")
                .setContentIntent(pendingIntent)
                .setNumber(clickTime++)
                .getNotification();

//        notification.icon = R.mipmap.ic_launcher;
//        notification.tickerText = "hello world";
//        notification.when = System.currentTimeMillis();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
//        notification.flags = Notification.DEFAULT_VIBRATE;
        manager.notify(clickTime, notification);
    }
}
