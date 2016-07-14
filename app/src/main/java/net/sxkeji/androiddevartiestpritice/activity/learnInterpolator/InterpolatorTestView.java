package net.sxkeji.androiddevartiestpritice.activity.learnInterpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.sxkeji.androiddevartiestpritice.R;

/**
 * 直接复制不好看，写个整体
 * Created by zhangshixin on 7/14/2016.
 */
public class InterpolatorTestView extends LinearLayout {
    private TextView tvName;    //加速器名称
    private ImageView imageView;

    public InterpolatorTestView(Context context) {
        super(context);
        init();
    }


    public InterpolatorTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InterpolatorTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_interpolator_view, null);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        imageView = (ImageView) view.findViewById(R.id.iv_img);
        addView(view);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);
        System.out.println("measureWidth :" + measureWidth);
        System.out.println("measureHeight :" + measureHeight);
        // 计算自定义的ViewGroup中所有子控件的大小
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int heighthMeasureSpec) {
        int result = 0;
        int heightMode = MeasureSpec.getMode(heighthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heighthMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }
        return result;
    }

    public void setName(String name) {
        if (tvName != null) {
            tvName.setText(name);
        }
    }

    public String getName() {
        if (tvName != null) {
            return tvName.getText().toString();
        }
        return null;
    }

    public TextView getTvName() {
        return tvName;
    }

    public void setTvName(TextView tvName) {
        this.tvName = tvName;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
