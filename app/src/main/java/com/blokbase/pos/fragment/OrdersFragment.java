package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blokbase.pos.R;
import com.common.lib.fragment.BaseFragment;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;

import java.util.ArrayList;

public class OrdersFragment extends BaseFragment<EmptyContract.Presenter>
        implements EmptyContract.View {

    private ArrayList<OrderListFragment> mFragments;
    private int mCurrentItem;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_orders;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setViewsOnClickListener(R.id.llALl, R.id.llTradeSuccess, R.id.llTransfering);
        mCurrentItem = 0;
        mFragments = new ArrayList<>();
        mFragments.add(OrderListFragment.getInstance(0));
        mFragments.add(OrderListFragment.getInstance(10));
        mFragments.add(OrderListFragment.getInstance(20));
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
                resetTab();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(mCurrentItem);
    }

    public void onRefresh() {
        if (getView() == null || mFragments == null || mFragments.isEmpty()) {
            return;
        }
        mFragments.get(mCurrentItem).onRefresh();
    }

    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llALl:
                mCurrentItem = 0;
                resetTab();
                ViewPager viewPager = getView().findViewById(R.id.viewPager);
                viewPager.setCurrentItem(0);
                break;
            case R.id.llTradeSuccess:
                mCurrentItem = 1;
                resetTab();
                viewPager = getView().findViewById(R.id.viewPager);
                viewPager.setCurrentItem(1);
                break;
            case R.id.llTransfering:
                mCurrentItem = 2;
                resetTab();
                viewPager = getView().findViewById(R.id.viewPager);
                viewPager.setCurrentItem(2);
                break;
        }
    }

    private void resetTab() {
        LinearLayout llBar = getView().findViewById(R.id.llBar);
        int size = llBar.getChildCount();
        for (int i = 0; i < size; ++i) {
            TextView tv = (TextView) ((ViewGroup) llBar.getChildAt(i)).getChildAt(0);
            View line = ((ViewGroup) llBar.getChildAt(i)).getChildAt(1);
            line.setVisibility(mCurrentItem == i ? View.VISIBLE : View.INVISIBLE);
            tv.setTextColor(ContextCompat.getColor(getActivity(), mCurrentItem == i ? R.color.text_color_5 : R.color.text_color_2));
        }
    }


}
