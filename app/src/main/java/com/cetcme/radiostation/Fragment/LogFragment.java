package com.cetcme.radiostation.Fragment;

import android.support.v4.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cetcme.radiostation.R;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

import java.util.ArrayList;
import java.util.List;


public class LogFragment extends Fragment {

    private String TAG = "LogFragment";

    /**
     * 顶部2个LinearLayout
     */
    private LinearLayout mTab1;
    private LinearLayout mTab2;
    private LinearLayout mTab3;

    /**
     * 顶部的3个TextView
     */
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    /**
     * Tab的那个引导线
     */
    private ImageView mTabLine;

    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    private ViewPager mViewPager;
    private FragmentAdapter mAdapter;
    private List<Fragment> fragments = new ArrayList<>();

    private Resources res;


    public static LogFragment newInstance(String param1) {
        LogFragment fragment = new LogFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public LogFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);
//        Bundle bundle = getArguments();
//        String agrs1 = bundle.getString("agrs1");

        initTitleView(view);

        res = getResources();
        initViewPager(view);

        return view;
    }

    private void initTitleView(View view) {
        QHTitleView qhTitleView = (QHTitleView) view.findViewById(R.id.qhTitleView);
        qhTitleView.setTitle(getString(R.string.main_tab_name_3));
        qhTitleView.setBackView(0);
        qhTitleView.setRightView(0);
        qhTitleView.setClickCallback(new QHTitleView.ClickCallback() {
            @Override
            public void onBackClick() {
                //
            }

            @Override
            public void onRightClick() {
                //
            }
        });
    }

    private void initViewPager(View view) {

        initView(view);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_in_log_fragment);

        /**
         * 初始化Adapter
         */

        //getFragmentManager到的是activity对所包含fragment的Manager
        //而如果是fragment嵌套fragment，那么就需要利用getChildFragmentManager()
        mAdapter = new FragmentAdapter(getChildFragmentManager(), fragments);

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new TabOnPageChangeListener());

        initTabLine(view);
    }

    /**
     * 根据屏幕的宽度，初始化引导线的宽度
     */
    private void initTabLine(View view) {
        mTabLine = (ImageView) view.findViewById(R.id.id_tab_line);

        //获取屏幕的宽度
        DisplayMetrics outMetrics = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;

        //获取控件的LayoutParams参数(注意：一定要用父控件的LayoutParams写LinearLayout.LayoutParams)
        LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) mTabLine.getLayoutParams();
        layoutParams.width = screenWidth / 3;//设置该控件的layoutParams参数
        mTabLine.setLayoutParams(layoutParams);//将修改好的layoutParams设置为该控件的layoutParams
    }

    /**
     * 初始化控件，初始化Fragment
     */
    private void initView(View view) {
        tv1 =(TextView) view.findViewById(R.id.id_tv1);
        tv2 =(TextView) view.findViewById(R.id.id_tv2);
        tv3 =(TextView) view.findViewById(R.id.id_tv3);

        tv1.setOnClickListener(new TabOnClickListener(0));
        tv2.setOnClickListener(new TabOnClickListener(1));
        tv3.setOnClickListener(new TabOnClickListener(2));

        fragments.clear();
        fragments.add(new LogDangerReceiveFragment());
        fragments.add(new LogDangerReceiveFragment());
        fragments.add(new LogDangerReceiveFragment());

        mTab1=(LinearLayout) view.findViewById(R.id.id_tab1);
        mTab2=(LinearLayout) view.findViewById(R.id.id_tab2);
        mTab3=(LinearLayout) view.findViewById(R.id.id_tab3);

        mTab1.setOnClickListener(new TabOnClickListener(0));
        mTab2.setOnClickListener(new TabOnClickListener(1));
        mTab3.setOnClickListener(new TabOnClickListener(2));
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        tv1.setTextColor(res.getColor(R.color.homepage_text_color));
        tv2.setTextColor(res.getColor(R.color.homepage_text_color));
        tv3.setTextColor(res.getColor(R.color.homepage_text_color));
    }

    /**
     * 功能：点击主页TAB事件
     */
    public class TabOnClickListener implements View.OnClickListener{
        private int index = 0;

        public TabOnClickListener(int i){
            index = i;
        }

        public void onClick(View v) {
            mViewPager.setCurrentItem(index);//选择某一页
        }

    }

    /**
     * 功能：Fragment页面改变事件
     */
    public class TabOnPageChangeListener implements ViewPager.OnPageChangeListener {

        //当滑动状态改变时调用
        public void onPageScrollStateChanged(int state) {

        }

        //当前页面被滑动时调用
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
            LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) mTabLine.getLayoutParams();
            //返回组件距离左侧组件的距离
            layoutParams.leftMargin= (int) ((positionOffset + position) * screenWidth / 3);
            mTabLine.setLayoutParams(layoutParams);
        }

        //当新的页面被选中时调用
        public void onPageSelected(int position) {
            //重置所有TextView的字体颜色
            resetTextView();
            switch (position) {
                case 0:
                    tv1.setTextColor(res.getColor(R.color.tab_text_selected));
                    break;
                case 1:
                    tv2.setTextColor(res.getColor(R.color.tab_text_selected));
                    break;
                case 2:
                    tv3.setTextColor(res.getColor(R.color.tab_text_selected));
                    break;
            }
        }
    }


    /**
     * 功能：主页引导栏的三个Fragment页面设置适配器
     */
    private class FragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments=fragments;
        }

        public Fragment getItem(int fragment) {
            return fragments.get(fragment);
        }

        public int getCount() {
            return fragments.size();
        }

    }


}
