package com.wzhnsc.firstwelcomescreen;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

class ViewPagerAdapter extends PagerAdapter
{
    // 页面视图列表
    private List<View> mViewList;

    public ViewPagerAdapter(List<View> ViewList)
    {
        mViewList = ViewList;
    }

    // 获取要滑动的页面视图的数量
    @Override
    public int getCount()
    {
        if (null != mViewList)
        {
            return mViewList.size();
        }

        return 0;
    }

    // 判断页面视图是否与该对象有联系
    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {
        return (arg0 == arg1);
    }

    // 如果滑动的图片进入了范围，将图片添加
    @Override
    public Object instantiateItem(View arg0, int arg1)
    {
        ((ViewPager)arg0).addView(mViewList.get(arg1), 0);

        return mViewList.get(arg1);
    }

    // 移除给定位置的页面
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2)
    {
        ((ViewPager)arg0).removeView(mViewList.get(arg1));
    }

    @Override
    public void finishUpdate(View arg0)
    {
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1)
    {
    }

    @Override
    public Parcelable saveState()
    {
        return null;
    }

    @Override
    public void startUpdate(View arg0)
    {
    }
}

public class GuideScreen extends Activity
                         implements OnPageChangeListener
{
    public static final String SP_FIRST_START = "FirstStart";

    private ViewPager        mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private List<View>       mViewList;

    // 底部圆点图片
    private ImageView[] mImageViews;

    // 记录当前选中位置
    private int mCurrentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.guide_screen);

        // 初始化滑动页面
        initViews();

        // 初始化底部圆点
        initDots();
    }

    private void initViews()
    {
        LayoutInflater inflater = getLayoutInflater();

        // 初始化引导图片列表
        mViewList = new ArrayList<View>();
        mViewList.add(inflater.inflate(R.layout.page1,   null));
        mViewList.add(inflater.inflate(R.layout.page2,   null));
        mViewList.add(inflater.inflate(R.layout.page3, null));

        View view = inflater.inflate(R.layout.page4, null);
        mViewList.add(view);

        ImageView ImageButton = (ImageView)view.findViewById(R.id.iv_start_weibo);
        ImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Editor editor = sp.edit();
                // 存入数据
                editor.putBoolean(SP_FIRST_START, false);
                // 提交修改
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
                {
                    editor.apply();
                }
                else
                {
                    editor.commit();
                }

//                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
//                startActivity(intent);

                finish();
            }
        });


        // 初始化Adapter
        mViewPagerAdapter = new ViewPagerAdapter(mViewList);

        mViewPager = (ViewPager)findViewById(R.id.viewpager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(this);
    }

    private void initDots()
    {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        mImageViews = new ImageView[mViewList.size()];

        // 循环取得小点图片
        for (int i = 0; i < mViewList.size(); i++)
        {
            mImageViews[i] = (ImageView)ll.getChildAt(i);
            // 设为灰色
            mImageViews[i].setEnabled(false);
        }

        mCurrentPosition = 0;

        // 选中状态，设置为白色
        mImageViews[mCurrentPosition].setEnabled(true);
    }

    // 当新的页面被选中时，修改对应底部圆点的状态和记录当前选中页面的索引号
    @Override
    public void onPageSelected(int arg0)
    {
        // 设置底部小点选中状态
        if (arg0 < 0
           || arg0 > mViewList.size() - 1
           || mCurrentPosition == arg0)
        {
            return;
        }

        mImageViews[arg0].setEnabled(true);
        mImageViews[mCurrentPosition].setEnabled(false);

        mCurrentPosition = arg0;
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0)
    {
    }

    // 当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2)
    {
    }
}
