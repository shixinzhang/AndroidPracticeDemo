package net.sxkeji.androiddevartiestpritice.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import net.sxkeji.androiddevartiestpritice.R;
import net.sxkeji.androiddevartiestpritice.service.LockScreenService;

/**
 * 开启锁屏
 * Created by zhangshixin on 5/3/2016.
 */
public class StartLockScreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_lock);
        startService(new Intent(this, LockScreenService.class));
        finish();
    }
}
