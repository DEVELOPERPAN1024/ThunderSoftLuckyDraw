package cn.thundersoft.codingnight.acitivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.thundersoft.codingnight.R;
import cn.thundersoft.codingnight.fragment.BaseFragment;
import me.majiajie.pagerbottomtabstrip.Controller;
import me.majiajie.pagerbottomtabstrip.PagerBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.TabItemBuilder;
import me.majiajie.pagerbottomtabstrip.TabLayoutMode;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectListener;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.main_bottom_bar)
    PagerBottomTabLayout mMainBottomBar;
    @Bind(R.id.main_viewpager)
    ViewPager mMainViewpager;

    private Controller mController;
    OnTabItemSelectListener mBottomTabListener = new OnTabItemSelectListener() {
        @Override
        public void onSelected(int index, Object tag)
        {
            mMainViewpager.setCurrentItem(index,false);
        }

        @Override
        public void onRepeatClick(int index, Object tag) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        initBottomTab();
        mMainViewpager.setAdapter(new MainFragmentAdapter(getSupportFragmentManager()));
        mMainViewpager.setOffscreenPageLimit(3);
        mMainViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mController.setSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBottomTab()
    {

        //用TabItemBuilder构建一个导航按钮
        TabItemBuilder tabItemBuilder = new TabItemBuilder(this).create()
                .setDefaultIcon(android.R.drawable.ic_btn_speak_now)
                .setText("one")
                .setSelectedColor(getResources().getColor(R.color.colorPrimary))
                .setTag("A")
                .build();

        //构建导航栏,得到Controller进行后续控制
        mController = mMainBottomBar.builder()
                .addTabItem(tabItemBuilder)
                .addTabItem(android.R.drawable.ic_dialog_email, "two",getResources().getColor(R.color.colorPrimary))
                .addTabItem(android.R.drawable.ic_dialog_info, "three",getResources().getColor(R.color.colorPrimary))
                .addTabItem(android.R.drawable.ic_dialog_map, "four",getResources().getColor(R.color.colorPrimary))
                .setMode(TabLayoutMode.HIDE_TEXT| TabLayoutMode.CHANGE_BACKGROUND_COLOR)
                .build();

        mController.addTabItemClickListener(mBottomTabListener);
    }

    private class MainFragmentAdapter extends FragmentPagerAdapter {

        FragmentManager fm;

        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    BaseFragment fragmentOne;
                    fragmentOne = (BaseFragment) fm.findFragmentByTag("find");

                    if (fragmentOne == null) {
                        fragmentOne = new BaseFragment();
                    }
                    //实际开发的时候请去除
                    fragmentOne.setArguments(getPrivateBundle("one"));
                    return fragmentOne;
                }
                case 1: {
                    BaseFragment fragmentTwo;
                    fragmentTwo = (BaseFragment) fm.findFragmentByTag("pic");
                    if (fragmentTwo == null) {
                        fragmentTwo = new BaseFragment();
                    }
                    fragmentTwo.setArguments(getPrivateBundle("two"));
                    return fragmentTwo;
                }
                case 2: {
                    BaseFragment fragmentThree;
                    fragmentThree = (BaseFragment) fm.findFragmentByTag("box");
                    if (fragmentThree == null) {
                        fragmentThree = new BaseFragment();
                    }
                    fragmentThree.setArguments(getPrivateBundle("three"));
                    return fragmentThree;
                }
                case 3: {
                    BaseFragment fragmentFour;
                    fragmentFour = (BaseFragment) fm.findFragmentByTag("min");
                    if (fragmentFour == null) {
                        fragmentFour = new BaseFragment();
                    }
                    fragmentFour.setArguments(getPrivateBundle("four"));
                    return fragmentFour;
                }
                default:
                    break;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }

        private Bundle getPrivateBundle(String text){
            Bundle bundle = new Bundle();
            bundle.putString("demo",text);
            return bundle;
        }
    }
}
