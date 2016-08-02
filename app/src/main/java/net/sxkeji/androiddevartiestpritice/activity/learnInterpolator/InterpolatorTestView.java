package net.sxkeji.androiddevartiestpritice.activity.learnInterpolator;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.sxkeji.androiddevartiestpritice.R;

/**
 * 直接复制不好看，写个整体
 * Created by zhangshixin on 7/14/2016.
 */
public class InterpolatorTestView extends LinearLayout {
    private int screenWidth, screenHeight;
    private TextView tvName;    //加速器名称
    private ImageView imageView;
    private Context context;

    public InterpolatorTestView(Context context) {
        super(context);
        init(context);
    }


    public InterpolatorTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InterpolatorTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
//        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        screenHeight = 200;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_interpolator_view, null);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        imageView = (ImageView) view.findViewById(R.id.iv_img);
        addView(view);
    }

    //  7/14/2016 自定义控件长度为 match_parent 时不起作用？？   -------  需要手动设置为屏幕的宽度
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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.requestLayout();
        }
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                result = screenWidth;
                break;
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = screenHeight;
                break;
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
