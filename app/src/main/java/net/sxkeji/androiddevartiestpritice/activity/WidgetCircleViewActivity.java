package net.sxkeji.androiddevartiestpritice.activity;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import net.sxkeji.androiddevartiestpritice.R;
import net.sxkeji.androiddevartiestpritice.widget.CircleView;

/**
 * Created by zhangshixin on 4/26/2016.
 */

public class WidgetCircleViewActivity extends Activity {

    private CircleView mCircleView;
    private SeekBar mSeekBar;
    private TextView power;
    private int max = 1024;
    private int min = 102;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                int num = msg.getData().getInt("progress");
                Log.i("num", num + "");
                power.setText((float) num / 100 * max + "M/" + max + "M");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_circle_view);

        power = (TextView) findViewById(R.id.power);
        power.setText(min + "M/" + max + "M");

        mCircleView = (CircleView) findViewById(R.id.wave_view);
        // 设置多高，float，0.1-1F
        mCircleView.setmWaterLevel(0.1F);
        // 开始执行
        mCircleView.startWave();

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mCircleView.setmWaterLevel((float) progress / 100);
                // 创建一个消息
                Message message = new Message();
                Bundle bundle = new Bundle();
                // put一个int值
                bundle.putInt("progress", progress);
                // 装载
                message.setData(bundle);
                // 发送消息
                handler.sendMessage(message);
                // 创建表示
                message.what = 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        mCircleView.stopWave();
        mCircleView = null;
        super.onDestroy();
    }
}
