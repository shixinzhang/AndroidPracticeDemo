package net.sxkeji.androiddevartiestpritice.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import net.sxkeji.androiddevartiestpritice.activity.LockScreenActivity;

/**
 * 锁屏
 * Created by zhangshixin on 5/3/2016.
 */
@SuppressWarnings("deprecation")
public class LockScreenService extends Service {
    private String TAG = "LockScreenService";
    private KeyguardManager keyguardManager = null;
    private KeyguardManager.KeyguardLock keyguardLock = null;
    Intent toMainIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e(TAG, "LockScreenService onCreate");
        toMainIntent = new Intent(LockScreenService.this, LockScreenActivity.class);
        toMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        registerReceiver(screenReceiver, intentFilter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenReceiver);
        startActivity(new Intent(LockScreenService.this,LockScreenService.class));
    }

    private BroadcastReceiver screenReceiver = new BroadcastReceiver() {

        @SuppressWarnings("static-access")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, "intent.action = " + action);

//            if (action.equals("android.intent.action.SCREEN_ON") || action.equals("android.intent.action.SCREEN_OFF")) {
                if (action.equals("android.intent.action.SCREEN_ON")) {

                keyguardManager = (KeyguardManager) context.getSystemService(context.KEYGUARD_SERVICE);
                keyguardLock = keyguardManager.newKeyguardLock("");
                keyguardLock.disableKeyguard();
                Log.e("", "closed the keyGuard");

                startActivity(toMainIntent);
            }

        }
    };

}