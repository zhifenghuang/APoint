package com.blokbase.pos.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blokbase.pos.R;
import com.blokbase.pos.fragment.PermissionRecordFragment;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.ObserverBean;
import com.common.lib.constant.Constants;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;

import java.util.ArrayList;

public class ObserverPermissionRecordActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {

    private ArrayList<BaseFragment> mFragments;
    private int mCurrentItem;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_observer_permission_record;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setViewsOnClickListener(R.id.llPermissionRecord1, R.id.llPermissionRecord2);
        ObserverBean bean = (ObserverBean) getIntent().getExtras().getSerializable(Constants.BUNDLE_EXTRA);
        mCurrentItem = 0;
        mFragments = new ArrayList<>();
        mFragments.add(PermissionRecordFragment.getInstance("income", bean.getObserverId()));
        mFragments.add(PermissionRecordFragment.getInstance("account", bean.getObserverId()));
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
                resetBtn(findViewById(R.id.llPermissionRecord1), findViewById(R.id.llPermissionRecord2));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(mCurrentItem);
    }

    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llPermissionRecord1:
                if (mCurrentItem == 0) {
                    return;
                }
                mCurrentItem = 0;
                ViewPager viewPager = findViewById(R.id.viewPager);
                viewPager.setCurrentItem(0);
                resetBtn(findViewById(R.id.llPermissionRecord1), findViewById(R.id.llPermissionRecord2));
                break;
            case R.id.llPermissionRecord2:
                if (mCurrentItem == 1) {
                    return;
                }
                mCurrentItem = 1;
                viewPager = findViewById(R.id.viewPager);
                viewPager.setCurrentItem(1);
                resetBtn(findViewById(R.id.llPermissionRecord1), findViewById(R.id.llPermissionRecord2));
                break;
        }
    }

    private void resetBtn(LinearLayout... lls) {
        int index = 0;
        for (LinearLayout ll : lls) {
            TextView tv = (TextView) ll.getChildAt(0);
            View line = ll.getChildAt(1);
            if (index == mCurrentItem) {
                tv.setTextColor(ContextCompat.getColor(this, R.color.text_color_1));
                tv.getPaint().setTypeface(Typeface.DEFAULT_BOLD);
                line.setVisibility(View.VISIBLE);
            } else {
                tv.setTextColor(ContextCompat.getColor(this, R.color.text_color_2));
                tv.getPaint().setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                line.setVisibility(View.INVISIBLE);
            }
            ++index;
        }
    }
}
