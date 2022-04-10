package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blokbase.pos.R;
import com.blokbase.pos.activity.MineActivity;
import com.common.lib.bean.HomeDataBean;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;

import java.util.ArrayList;

public class PoolFragment extends BaseFragment<EmptyContract.Presenter> implements EmptyContract.View {

    private ArrayList<BaseFragment> mFragments;
    private int mCurrentItem;


    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pool;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(R.id.llTop);
        setViewsOnClickListener(R.id.ivProfile, R.id.ivMore, R.id.tvPosPool, R.id.tvPosrPool);
        mCurrentItem = 0;
        mFragments = new ArrayList<>();
        mFragments.add(new PosPoolFragment());
        //     mFragments.add(new PosrPoolFragment());
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(mCurrentItem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivProfile:
            case R.id.ivMore:
                openActivity(MineActivity.class);
                break;
            case R.id.tvPosPool:
                break;
            case R.id.tvPosrPool:
                showToast(R.string.app_not_open);
                break;
        }
    }
}
