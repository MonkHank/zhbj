package com.seuic.zhbj.base.impl.menu;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.seuic.zhbj.R;
import com.seuic.zhbj.base.BaseMenuDetailPager;
import com.seuic.zhbj.domain.PhotoBean;
import com.seuic.zhbj.global.GlobalConstant;
import com.seuic.zhbj.utils.CacheUtils;
import com.seuic.zhbj.utils.LogUtil;
import com.seuic.zhbj.utils.MyBitMapUtils;
import com.seuic.zhbj.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by bgl on 2017/4/26.
 */

/**
 * 菜单详情页  -- 组图
 */
public class PhotosMenuDetaiPager extends BaseMenuDetailPager implements View.OnClickListener {
    @ViewInject(R.id.lv_photo) ListView lvPhoto;
    @ViewInject(R.id.gv_photo)GridView gvPhoto;
    private ImageButton ibPhoto;

    private ArrayList<PhotoBean.PhotoNews> mNewsList;


    public PhotosMenuDetaiPager(Activity activity, ImageButton ibPhoto) {
        super(activity);
        this.ibPhoto = ibPhoto;
        ibPhoto.setOnClickListener(this);
    }

    @Override
    public View initView() {
        /*TextView textView = new TextView(mActivity);
        textView.setText("菜单详情页  -- 组图");
        textView.setTextColor(Color.RED);
        textView.setTextSize(22);
        textView.setGravity(Gravity.CENTER);*/
        View view = View.inflate(mActivity, R.layout.photo_menu_detail_pager, null);
//        ViewUtils.inject(view); // 不是这个
        ViewUtils.inject(this,view);


        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(GlobalConstant.PHOTO_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, GlobalConstant.PHOTO_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
               String result = responseInfo.result;
                LogUtil.i(PhotosMenuDetaiPager.this,"result:"+result);
                processData(result);
                CacheUtils.setCache(GlobalConstant.PHOTO_URL,result,mActivity);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                ToastUtils.showToast(mActivity,s);
            }
        });
    }

    private void processData(String cache) {
        Gson gson = new Gson();
        PhotoBean photoBean = gson.fromJson(cache, PhotoBean.class);
        mNewsList = photoBean.data.news;
        lvPhoto.setAdapter(new PhotoAdapter());
        gvPhoto.setAdapter(new PhotoAdapter());// gridview的布局结构和listview完全一致，所以可以共用一个adapter；
    }

    private boolean isListView = true; // 标记当前是否是listview显示
    @Override
    public void onClick(View v) {
        if (isListView) {// 切换到 gridview
            lvPhoto.setVisibility(View.GONE);
            gvPhoto.setVisibility(View.VISIBLE);

            ibPhoto.setImageResource(R.mipmap.icon_pic_list_type);
            isListView = false;
        } else {// 切换到 listview
            lvPhoto.setVisibility(View.VISIBLE);
            gvPhoto.setVisibility(View.GONE);
            ibPhoto.setImageResource(R.mipmap.icon_pic_grid_type);
            isListView = true;
        }

    }

    class PhotoAdapter extends BaseAdapter{

//        private  BitmapUtils mBitmapUtils;
        private MyBitMapUtils mBitmapUtils;

        public PhotoAdapter() {
//            mBitmapUtils = new BitmapUtils(mActivity);
//            mBitmapUtils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
            mBitmapUtils = new MyBitMapUtils();
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public PhotoBean.PhotoNews getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_photos, null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PhotoBean.PhotoNews photoNews = getItem(position);
            holder.tvTitle.setText(photoNews.title);
            mBitmapUtils.display(holder.ivPic, photoNews.listimage);

            return convertView;
        }
    }
    static class ViewHolder{
        public ImageView ivPic;
        public TextView tvTitle;
    }

}
