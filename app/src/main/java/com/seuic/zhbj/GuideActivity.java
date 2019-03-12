package com.seuic.zhbj;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.seuic.zhbj.utils.DensityUtils;
import com.seuic.zhbj.utils.LogUtil;
import com.seuic.zhbj.utils.PrefUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dell on 2017/4/16.
 */

public class GuideActivity extends AppCompatActivity {
    @BindView(R.id.vp_guide_guide) ViewPager vp_guide_guide;
    @BindView(R.id.ll_guide_bot)  LinearLayout ll_guide_bot;
    @BindView(R.id.iv_red_point)  ImageView iv_red_point;// 小红点
    @BindView(R.id.bt_guide_start) Button bt_guide_start;


    int[] resID = {R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};
    ArrayList<ImageView> mImageViewList;
    private int mPointDis; // 小红点移动距离

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        if (getSupportActionBar() != null) {
            ActionBar supportActionBar = getSupportActionBar();
            supportActionBar.hide();
        }
        ButterKnife.bind(this);

        init();
        vp_guide_guide.setAdapter(new MyAdapter());
        // 计算圆点距离  第2个远点left - 第1个远点left
        // 在界面上画一个控件要经过 measure->layout(确定位置)->draw; 此流程是onCreat方法结束后才会走此流程；
//        mPointDis = ll_guide_bot.getChildAt(1).getLeft() - ll_guide_bot.getChildAt(0).getLeft();
//        LogUtil.i("小红点距离："+mPointDis);// mPointDis = 0;
        // 监听layout方法结束的事件，位置确定好之后在获取远点距离
        // 获取小红点视图树监听
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                iv_red_point.getViewTreeObserver().removeOnGlobalLayoutListener(this);// 16以后
                //移除监听，避免重复执行onGlobalLayout方法
                iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mPointDis = ll_guide_bot.getChildAt(1).getLeft() - ll_guide_bot.getChildAt(0).getLeft();
                LogUtil.i(this,"小红点距离："+mPointDis);
            }
        });

        vp_guide_guide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {// 页面滑动过程中不断调用
                // 更新小红点的移动距离， 计算小红点当前的左边距
                int leftMarin = (int) (mPointDis * positionOffset)+ position * mPointDis; // 距离 * 移动百分比
                // 拿到布局参数，根据父控件来拿
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
                params.leftMargin = leftMarin; //　修改左边距
                // 重新设置布局参数
                iv_red_point.setLayoutParams(params);

            }

            @Override
            public void onPageSelected(int position) {  // 某个页面被选中
                if (position == mImageViewList.size() - 1)
                    bt_guide_start.setVisibility(View.VISIBLE);
                else
                    bt_guide_start.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bt_guide_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.setBoolean(getApplicationContext(),"isFirst",false);
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void init() {
        ImageView imageViewGuide;
        ImageView imageViewBot;
        mImageViewList = new ArrayList<ImageView>();
        for(int i =0;i<3;i++){
            imageViewGuide = new ImageView(this);
            imageViewGuide.setBackgroundResource(resID[i]);
            mImageViewList.add(imageViewGuide);
            // 小圆点初始化
            imageViewBot = new ImageView(this);
            imageViewBot.setImageResource(R.drawable.shape_bot_huise);
            // 父控件是谁，就是谁声明的布局参数
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i>0){
                // 设置左边距
                layoutParams.leftMargin = DensityUtils.dip2px(10, this); // 10dp 转成像素
                imageViewBot.setLayoutParams(layoutParams);
            }
            ll_guide_bot.addView(imageViewBot);
        }
    }

    /**
     * viewPager的adapter
     */
    class MyAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mImageViewList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
