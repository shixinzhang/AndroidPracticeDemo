package net.sxkeji.androiddevartiestpritice.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.sxkeji.androiddevartiestpritice.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 锁屏界面
 * Created by zhangshixin on 5/3/2016.
 */
public class LockScreenActivity extends Activity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.btn_close)
    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        ButterKnife.bind(this);

        setListsners();
    }

    private void setListsners() {
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LockScreenActivity.this, "hello zsx", Toast.LENGTH_SHORT).show();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
                finish();
            }
        });
    }
}
