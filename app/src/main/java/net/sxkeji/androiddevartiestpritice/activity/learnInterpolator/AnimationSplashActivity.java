package net.sxkeji.androiddevartiestpritice.activity.learnInterpolator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import net.sxkeji.androiddevartiestpritice.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 熟悉 Interpolator
 * Created by zhangshixin on 7/13/2016.
 */
public class AnimationSplashActivity extends Activity {
    private final String TAG = "AnimationSplashActivity";
    @Bind(R.id.progress)
    ProgressBar progress;
    @Bind(R.id.iv_loading)
    ImageView ivLoading;
    @Bind(R.id.interpolator_1)
    InterpolatorTestView interpolator1;
    @Bind(R.id.interpolator_2)
    InterpolatorTestView interpolator2;
    @Bind(R.id.interpolator_3)
    InterpolatorTestView interpolator3;
    @Bind(R.id.interpolator_4)
    InterpolatorTestView interpolator4;
    @Bind(R.id.interpolator_5)
    InterpolatorTestView interpolator5;
    @Bind(R.id.interpolator_6)
    InterpolatorTestView interpolator6;
    @Bind(R.id.interpolator_7)
    InterpolatorTestView interpolator7;
    @Bind(R.id.interpolator_8)
    InterpolatorTestView interpolator8;
    @Bind(R.id.interpolator_9)
    InterpolatorTestView interpolator9;

    private List<AnimatorSet> interpolatorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        interpolatorList = new ArrayList<>();
        startAnim(interpolator1, 1);
        startAnim(interpolator2, 2);
        startAnim(interpolator3, 3);
        startAnim(interpolator4, 4);
        startAnim(interpolator5, 5);
        startAnim(interpolator6, 6);
        startAnim(interpolator7, 7);
        startAnim(interpolator8, 8);
        startAnim(interpolator9, 9);

    }

    private void startAnim(InterpolatorTestView testView, final int i) {
        ImageView imageView = testView.getImageView();
        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(imageView, View.ROTATION, 0, 1080).setDuration(4000);
        ObjectAnimator translationXAnim = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, 0, 600).setDuration(4000);
        //循环
        rotationAnim.setRepeatCount(ValueAnimator.INFINITE);
        translationXAnim.setRepeatCount(ValueAnimator.INFINITE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotationAnim)
                .with(translationXAnim);
        Interpolator interpolator = null;
        switch (i) {
            case 1:
                interpolator = new AccelerateDecelerateInterpolator();
                testView.setName("先加速后减速 AccelerateDecelerateInterpolator");
                break;
            case 2:
                interpolator = new DecelerateInterpolator();
                testView.setName("减速--------------------------DecelerateInterpolator----------");
                break;
            case 3:
                interpolator = new AccelerateInterpolator();
                testView.setName("加速----------------------------AccelerateInterpolator------------");
                break;
            case 4:
                interpolator = new AnticipateInterpolator();
                testView.setName("先后退一段然后冲刺到终止位置--AnticipateInterpolator-------------");
                break;
            case 5:
                interpolator = new AnticipateOvershootInterpolator();
                testView.setName("先后退一段然后冲刺过头，最后回到终止位置 AnticipateOvershootInterpolator");
                break;
            case 6:
                interpolator = new BounceInterpolator();
                testView.setName("结束时弹起-----------------------BounceInterpolator-------------");
                break;
            case 7:
                interpolator = new CycleInterpolator(3);
                testView.setName("循环播放特定次数（此处3），速率为正弦曲线--------CycleInterpolator");
                break;
            case 8:
                interpolator = new OvershootInterpolator(5);
                testView.setName("直接冲过头,冲出去多少取决于传的值，然后回去---OvershootInterpolator---");
                break;
            case 9:
                interpolator = new LinearInterpolator();
                testView.setName("匀速---------------------LinearInterpolator-----------------------");
                break;

        }
        animatorSet.setInterpolator(interpolator);

        animatorSet.start();
        interpolatorList.add(animatorSet);
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (AnimatorSet animatorSet : interpolatorList) {
            animatorSet.cancel();
        }
        interpolatorList.clear();
    }
}
