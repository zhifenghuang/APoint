package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blokbase.pos.R;
import com.blokbase.pos.fragment.PoolNodeRankFragment;
import com.common.lib.activity.BaseActivity;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;

import java.util.ArrayList;

public class PoolNodeRankActivity extends BaseActivity<EmptyContract.Presenter>
        implements EmptyContract.View {

    private ArrayList<PoolNodeRankFragment> mFragments;
    private int mCurrentItem;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pool_node;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setViewsOnClickListener(R.id.tvPool0, R.id.tvPool1);

        mCurrentItem = 0;
        mFragments = new ArrayList<>();
        mFragments.add(PoolNodeRankFragment.getInstance(10));
        mFragments.add(PoolNodeRankFragment.getInstance(20));
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
                resetBtn(findViewById(R.id.tvPool0), findViewById(R.id.tvPool1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(mCurrentItem);
    }

    private void resetBtn(TextView... tvs) {
        int index = 0;
        for (TextView tv : tvs) {
            tv.setTextColor(ContextCompat.getColor(this, index == mCurrentItem ?
                    R.color.text_color_1 : R.color.text_color_4));
            ++index;
        }
    }

    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPool0:
                mCurrentItem = 0;
                ViewPager viewPager = findViewById(R.id.viewPager);
                viewPager.setCurrentItem(mCurrentItem);
                resetBtn(findViewById(R.id.tvPool0), findViewById(R.id.tvPool1));
                break;
            case R.id.tvPool1:
                mCurrentItem = 1;
                viewPager = findViewById(R.id.viewPager);
                viewPager.setCurrentItem(mCurrentItem);
                resetBtn(findViewById(R.id.tvPool0), findViewById(R.id.tvPool1));
                break;
        }
    }
}
