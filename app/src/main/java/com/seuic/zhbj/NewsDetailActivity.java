package com.seuic.zhbj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

/**
 * Created by bgl on 2017/5/25.
 * 新闻详情
 */

public class NewsDetailActivity extends Activity implements View.OnClickListener{
    @ViewInject(R.id.ll_control)LinearLayout llControl;
    @ViewInject(R.id.ib_menu)ImageButton ibMenu;
    @ViewInject(R.id.ib_back)ImageButton ibBack;
    @ViewInject(R.id.tv_title)TextView tvTitle;
    @ViewInject(R.id.ib_share)ImageButton ibShare;
    @ViewInject(R.id.ib_textsize)ImageButton ibTextSize;
    @ViewInject(R.id.wv_news_detail)WebView mWebView;
    @ViewInject(R.id.pb_loading)ProgressBar pbLoading;
    private String mUrl;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ShareSDK.initSDK(this);
        ViewUtils.inject(this);
        llControl.setVisibility(View.VISIBLE);
        ibBack.setVisibility(View.VISIBLE);
        ibMenu.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);

        ibShare.setOnClickListener(this);
        ibTextSize.setOnClickListener(this);
        ibBack.setOnClickListener(this);

        mUrl = getIntent().getStringExtra("url");
//        mWebView.loadUrl("http://www.itheima.com");
        mWebView.loadUrl(mUrl);
        WebSettings settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true);// 显示缩放按钮(wap网页不支持)
        settings.setUseWideViewPort(true);// 支持双击缩放(wap网页不支持)
        settings.setJavaScriptEnabled(true); // 支持js功能
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pbLoading.setVisibility(View.INVISIBLE);
                super.onPageFinished(view, url);
            }
            // 所有连接跳转会走此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);// 在跳转链接时强制在当前webview中加载
                return true;
            }
        });

//        mWebView.goBack();// 跳到上一个页面
//        mWebView.goForward();// 跳到下一个页面
        mWebView.setWebChromeClient(new WebChromeClient(){
            // 进度变化
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            // 网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;

            case R.id.ib_textsize:
                showChooseDialog();
                break;

            case R.id.ib_share:
                showShare();
                break;

        }
    }

    private int mTempWhich; // 记录临时选择字体大小(点击确定之前的)
    private int mCurrentWhich = 2; // 记录当前选中字体大小(点击确定之后的)
    // 展示选择字体的弹窗
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        String [] items = {"超大号字体", "大号字体","正常字体","小号字体","超小号字体"};
        builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 根据选择的字体修改网页字体的大小
                mTempWhich = which;

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = mWebView.getSettings();
                switch (mTempWhich) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
//                        settings.setTextZoom(22);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                mCurrentWhich = mTempWhich;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        // 修改样式
        oks.setTheme(OnekeyShareTheme.SKYBLUE);

        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}
