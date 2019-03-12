package com.seuic.zhbj.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.seuic.zhbj.MainActivity;
import com.seuic.zhbj.R;
import com.seuic.zhbj.base.impl.NewsCenterPager;
import com.seuic.zhbj.domain.NewsMenu;

import java.util.ArrayList;

/**
 * Created by someone on 2017/4/23.
 *
 * LeftMenuFragment 和 NewsCenterPager 之间互动；
 */

public class LeftMenuFragment extends BaseFragment {

    @ViewInject(R.id.lv_list)   ListView lvList;

    private ArrayList<NewsMenu.NewsMenuData> mNewsMenuData;//侧边栏网络数据对象
    private int mCurrentPos = 0; // 默认被选中的item的位置
    private LeftMenuAdapter mAdapter;

    @Override
    public View initView() { // onCreateView里面调用
        View view = View.inflate(mActivity, R.layout.fragment_left_menu,null);
//        lvList = (ListView) view.findViewById(R.id.lv_list);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {

    }

    /**
     * 给侧边栏设置数据
     * @param data
     */
    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data) {
        mCurrentPos = 0;// 当前选中位置归零
        // 更新页面
        mNewsMenuData = data;
        mAdapter = new LeftMenuAdapter();
        lvList.setAdapter(mAdapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position; // 更新当前被选中的位置
                mAdapter.notifyDataSetChanged();// 刷新listview
                // 收起侧边栏
                toggle();
                // 侧边栏点击之后，修改framlayout的内容
                setCurrentDetaiPager(position);
            }
        });
    }

    /**
     * 设置当前菜单详情页
     * @param position
     */
    private void setCurrentDetaiPager(int position) {
        MainActivity mainUi = (MainActivity) mActivity;
        ContentFragment contentFragment = mainUi.getContentFragment();
        // 获取新闻中心 NewsCenterPager
        NewsCenterPager newsCenterPager = contentFragment.getNewsCenterPager();
        // 修改新闻中心的FrameLayout
        newsCenterPager.setCurrentDetailPager(position);

    }

    /**
     * 打开或者关闭侧边栏
     */
    private void toggle() {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        slidingMenu.toggle();// 如果当前状态是开，调用后就关，反之亦然；
    }

    class LeftMenuAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int position) {
            return mNewsMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_item_menu, null);
            TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);
            NewsMenu.NewsMenuData item = getItem(position);
            tvMenu.setText(item.title);
            if (position == mCurrentPos) {
                // 被选中
                tvMenu.setEnabled(true); // 文字变成红色
            }else {
                // 未选中
                tvMenu.setEnabled(false);// 文字变成白色
            }
            return view;
        }
    }
}
