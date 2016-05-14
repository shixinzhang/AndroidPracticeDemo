package net.sxkeji.androiddevartiestpritice.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import net.sxkeji.androiddevartiestpritice.R;

/**
 * 自定义小工具
 * Created by zhangshixin on 4/20/2016.
 */
public class MyAppWidgetProvider extends AppWidgetProvider {
    private final String TAG = "MyAppWidgetProvider";
    public static final String CLICK_ACTION = "net.sxkeji.androiddevartiestpritice.action.CLICK";

    public MyAppWidgetProvider() {
        super();
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        String action = intent.getAction();
        Log.e(TAG, "onReceive :action = " + action);
        if (action.equals(CLICK_ACTION)) {
            Toast.makeText(context, "click my appwidget", Toast.LENGTH_SHORT).show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_ez);

                    for (int i = 0 ; i < 37 ; i ++){
                        float degree = (i * 10) % 360;
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.remoteview_widget);
                        remoteViews.setImageViewBitmap(R.id.iv_widget_img, rotateBitmap(context, bitmap, degree));

                        Intent intentClick = new Intent();
                        intentClick.setAction(CLICK_ACTION);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
                        remoteViews.setOnClickPendingIntent(R.id.iv_widget_img, pendingIntent);

                        appWidgetManager.updateAppWidget(new ComponentName(context, MyAppWidgetProvider.class),
                                remoteViews);

                        SystemClock.sleep(30);
                    }
                }
            }).start();
        }

    }

    /***
     * 每次桌面部件更新时都调用该方法
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.e(TAG, "onUpdate");

        int length = appWidgetIds.length;
        Log.e(TAG, "appWidgetIds.length " + length);

        for (int i = 0; i < length; i++) {
            int appWidgetId = appWidgetIds[i];
            onWidgetUpdate(context, appWidgetManager, appWidgetId);
        }
    }

    /**
     * 桌面小部件的更新
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    private void onWidgetUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.e(TAG, "onWidgetUpdate : appWidgetId = " + appWidgetId);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.remoteview_widget);

        //小部件单击时间发送的intent 广播
        Intent intentClick = new Intent();
        intentClick.setAction(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentClick, 0);
        remoteViews.setOnClickPendingIntent(R.id.iv_widget_img, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    /**
     * 旋转图片
     *
     * @param context
     * @param bitmap
     * @param degree
     * @return
     */
    private Bitmap rotateBitmap(Context context, Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap tempBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return tempBitmap;
    }
}
