package net.sxkeji.androiddevartiestpritice.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * 测试使用 Scroller 的自定义 View
 * Created by zhangshixin on 6/30/2016.
 */
public class ScrollerTestView extends ViewGroup {
    private final String TAG = "ScrollerTestView";
    private static final float SNAP_VELOCITY = 600;
    private Context context;
    private Scroller scroller;
    private float oldX;
    private int pointerId;  //手指id
    private int currentIndex;   //当前所在页面索引
    private VelocityTracker velocityTracker;
    private LinearLayout layout1, layout2, layout3;
    private int screenWidth, screenHeight;

    public ScrollerTestView(Context context) {
        super(context);
        init(context);
    }

    public ScrollerTestView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ScrollerTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        this.context = context;
        scroller = new Scroller(context);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        //add 3 views in the ViewGroup
        layout1 = new LinearLayout(context);
        layout1.setBackgroundColor(Color.RED);
        TextView textView = new TextView(context);
        textView.setText("向右滑动");
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        textView.setLayoutParams(layoutParams);

        layout1.addView(textView);
        addView(layout1);

        layout2 = new LinearLayout(context);
        layout2.setBackgroundColor(Color.GREEN);
        addView(layout2);

        layout3 = new LinearLayout(context);
        layout3.setBackgroundColor(Color.BLUE);
        addView(layout3);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 区别？
//        int w = MeasureSpec.getSize(widthMeasureSpec);
//        int h = MeasureSpec.getSize(heightMeasureSpec);
//        setMeasuredDimension(w, h);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.measure(screenWidth, screenHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int startX = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.layout(startX, 0, screenWidth + startX, screenHeight);
            startX += screenWidth;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                pointerId = event.getPointerId(0);      //获取第一个放下的手指id
                if (!scroller.isFinished()) {       //强制结束之前的滑动
                    scroller.abortAnimation();
                }
                oldX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                //根据之前的pointerId获取对应的移动轨迹
                int pointerIndex = event.findPointerIndex(pointerId);

                float x2 = event.getX(pointerIndex);
                Log.d(TAG, "onTouchEvent pointerId " + pointerId + "/ pointerIndex " + pointerIndex + " / x2 " + x2 + " / oldX " + oldX);
                int offset = (int) (oldX - x2);
                scrollBy(offset, 0);
                oldX = x2;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
                //计算速率
                float xVelocity = velocityTracker.getXVelocity(pointerId);
                if (xVelocity > SNAP_VELOCITY && currentIndex > 0) { //右划超过一定距离，调到上一个
                    snapToScreen(currentIndex - 1);
                } else if (xVelocity < -SNAP_VELOCITY && currentIndex < (getChildCount() - 1)) {  //左划超过一定距离,调到下一个
                    snapToScreen(currentIndex + 1);
                }
                //以上是 快速移动的
                //下面是缓慢移动，判断是否切换到下一屏幕
                else {
                    snapToDestination();
                }

                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:     // 多指触控
                //离开屏幕手指的index
                int leavePointerIndex = event.getActionIndex();
                int leavePointerId = event.getPointerId(leavePointerIndex);
                //
                if (leavePointerId == pointerId) {
                    int reIndex = leavePointerIndex == 0 ? 1 : 0;
                    pointerId = reIndex;

                    oldX = event.getX(reIndex);
                    if (velocityTracker != null) {
                        velocityTracker.recycle();
                        velocityTracker = null;
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 根据滑动距离是否超过一半，切换到指定目的地
     */
    private void snapToDestination() {
        int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;

        Log.d(TAG, "snapToDestination+++++  destScreen: " + destScreen);
        snapToScreen(destScreen);
    }

    /**
     * 滑动到指定屏幕
     *
     * @param i
     */
    private void snapToScreen(int i) {
        currentIndex = i;
        //防止屏幕越界
        if (currentIndex > getChildCount() - 1) {
            currentIndex = getChildCount() - 1;
        }
        //继续滚动
        int deltaX = currentIndex * screenWidth - getScrollX();
        scroller.startScroll(getScrollX(), 0, deltaX, 0, Math.abs(deltaX) * 2);
        Log.d(TAG, "snapToScreen+++++  currentIndex: " + currentIndex + " / deltaX " + deltaX);
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
}
