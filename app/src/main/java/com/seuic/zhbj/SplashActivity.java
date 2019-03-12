package com.seuic.zhbj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.seuic.zhbj.utils.PrefUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.rl_splash_root)RelativeLayout rl_splash_root;
    @BindView(R.id.iv_splash_horse)ImageView iv_splash_horse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// 继承AppCompatActivity无法通过此方法移除标题栏
        setContentView(R.layout.activity_splash);
//        getSupportActionBar().hide();
        ButterKnife.bind(this);

        // 旋转动画 (从0到360顺时针，x方向相对于自身，0.5f的坐标点；y同理)
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setDuration(1000); // 动画时间
        rotateAnimation.setFillAfter(true); //  保持动画结束状态

        // 缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0,1,Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);

        // 渐变动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);

        // 动画集合
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);

        // 执行动画
        rl_splash_root.setAnimation(animationSet);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 动画结束，执行页面跳转
                if (PrefUtils.getBoolean(SplashActivity.this, "isFirst",true)) {
                    Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
