package net.sxkeji.androiddevartiestpritice.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import net.sxkeji.androiddevartiestpritice.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * TimePicker 源码解析
 * Created by zhangshixin on 4/25/2016.
 */
public class TimePickerParseActivity extends Activity {
    private final String TAG = "TimePickerParseActivity";
    @Bind(R.id.timePicker)
    TimePicker timePicker;
    @Bind(R.id.switch_change_tp_mode)
    Switch switchChangeTpMode;
    @Bind(R.id.numberPicker)
    NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker_parse);
        ButterKnife.bind(this);
        initViews();
        setListeners();
    }

    private void setListeners() {
        switchChangeTpMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                showToast("isChecked " + isChecked);
            }
        });
    }

    private void initViews() {
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(59);
        numberPicker.setWrapSelectorWheel(true);

        timePicker.setIs24HourView(false);
//        timePicker.setEnabled(false);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                showToast(hourOfDay + "点 " + minute + "分");
                Log.e(TAG, " setOnTimeChangedListener is called : " + hourOfDay + " : " + minute + " / baseLine" + timePicker.getBaseline());
            }
        });
        ;
    }

    private void showToast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
