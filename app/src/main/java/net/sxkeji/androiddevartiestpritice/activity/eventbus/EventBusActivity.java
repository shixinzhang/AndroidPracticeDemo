package net.sxkeji.androiddevartiestpritice.activity.eventbus;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import net.sxkeji.androiddevartiestpritice.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * EventBus 学习
 * https://github.com/greenrobot/EventBus
 * http://greenrobot.org/eventbus/documentation/how-to-get-started/
 * <p/>
 * Created by zhangshixin on 7/8/2016.
 */
public class EventBusActivity extends Activity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbus);
        EventBus.getDefault().register(this);
        textView = (TextView) findViewById(R.id.tv_change);

        new Thread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new MessageEvent("new msg post by EventBus"));
            }
        }).start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event){
        if (event != null && !TextUtils.isEmpty(event.getMessage())){
            textView.setText(event.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
