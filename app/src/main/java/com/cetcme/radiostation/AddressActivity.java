package com.cetcme.radiostation;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cetcme.radiostation.Address.AddressShipFragment;
import com.qiuhong.qhlibrary.QHTitleView.QHTitleView;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity {

    private String TAG = "AddressActivity";

    /**
     * 顶部4个LinearLayout
     */
    private LinearLayout mTab1;
    private LinearLayout mTab2;
    private LinearLayout mTab3;
    private LinearLayout mTab4;

    /**
     * 顶部的4个TextView
     */
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    /**
     * Tab的那个引导线
     */
    private ImageView mTabLine;

    /**
     * 屏幕的宽度
     */
    private int screenWidth;

    private ViewPager mViewPager;
    private AddressActivity.FragmentAdapter mAdapter;
    private List<Fragment> fragments = new ArrayList<>();

    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        getSupportActionBar().hide();
        initTitleView();
        res = getResources();
        initViewPager();
    }

    private void initTitleView() {
        QHTitleView qhTitleView = (QHTitleView) findViewById(R.id.qhTitleView);
        qhTitleView.setTitle("地址簿");
        qhTitleView.setBackView(R.drawable.icon_back_button);
        qhTitleView.setRightView(R.drawable.icon_add);
        qhTitleView.setClickCallback(new QHTitleView.ClickCallback() {
            @Override
            public void onBackClick() {
                onBackPressed();
                //
            }

            @Override
            public void onRightClick() {
                //TODO: add
                Toast.makeText(AddressActivity.this, "add", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViewPager() {

        initView();

        mViewPager = (ViewPager) findViewById(R.id.viewpager_in_log_fragment);

        /**
         * 初始化Adapter
         */

        //getFragmentManager到的是activity对所包含fragment的Manager
        //而如果是fragment嵌套fragment，那么就需要利用getChildFragmentManager()
        mAdapter = new AddressActivity.FragmentAdapter(getSupportFragmentManager(), fragments);

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new AddressActivity.TabOnPageChangeListener());

        initTabLine();
    }

    /**
     * 根据屏幕的宽度，初始化引导线的宽度
     */
    private void initTabLine() {
        mTabLine = (ImageView) findViewById(R.id.id_tab_line);

        //获取屏幕的宽度
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;

        //获取控件的LayoutParams参数(注意：一定要用父控件的LayoutParams写LinearLayout.LayoutParams)
        LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) mTabLine.getLayoutParams();
        layoutParams.width = screenWidth / 4;//设置该控件的layoutParams参数
        mTabLine.setLayoutParams(layoutParams);//将修改好的layoutParams设置为该控件的layoutParams
    }

    /**
     * 初始化控件，初始化Fragment
     */
    private void initView() {
        tv1 =(TextView) findViewById(R.id.id_tv1);
        tv2 =(TextView) findViewById(R.id.id_tv2);
        tv3 =(TextView) findViewById(R.id.id_tv3);
        tv4 =(TextView) findViewById(R.id.id_tv4);

        tv1.setOnClickListener(new AddressActivity.TabOnClickListener(0));
        tv2.setOnClickListener(new AddressActivity.TabOnClickListener(1));
        tv3.setOnClickListener(new AddressActivity.TabOnClickListener(2));
        tv4.setOnClickListener(new AddressActivity.TabOnClickListener(2));

        fragments.add(new AddressShipFragment());
        fragments.add(new AddressShipFragment());
        fragments.add(new AddressShipFragment());
        fragments.add(new AddressShipFragment());

        mTab1=(LinearLayout) findViewById(R.id.id_tab1);
        mTab2=(LinearLayout) findViewById(R.id.id_tab2);
        mTab3=(LinearLayout) findViewById(R.id.id_tab3);
        mTab4=(LinearLayout) findViewById(R.id.id_tab4);

        mTab1.setOnClickListener(new AddressActivity.TabOnClickListener(0));
        mTab2.setOnClickListener(new AddressActivity.TabOnClickListener(1));
        mTab3.setOnClickListener(new AddressActivity.TabOnClickListener(2));
        mTab4.setOnClickListener(new AddressActivity.TabOnClickListener(3));
    }

    /**
     * 重置颜色
     */
    private void resetTextView() {
        tv1.setTextColor(res.getColor(R.color.homepage_text_color));
        tv2.setTextColor(res.getColor(R.color.homepage_text_color));
        tv3.setTextColor(res.getColor(R.color.homepage_text_color));
        tv4.setTextColor(res.getColor(R.color.homepage_text_color));
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
            layoutParams.leftMargin= (int) ((positionOffset + position) * screenWidth / 4);
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
                case 4:
                    tv4.setTextColor(res.getColor(R.color.tab_text_selected));
                    break;
            }
        }
    }


    /**
     * 功能：主页引导栏的4个Fragment页面设置适配器
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_right_in_no_alpha,
                R.anim.push_right_out_no_alpha);
    }
}
