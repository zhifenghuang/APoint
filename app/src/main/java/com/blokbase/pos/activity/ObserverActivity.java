package com.blokbase.pos.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.blokbase.pos.R;
import com.blokbase.pos.fragment.CollectionFragment;
import com.blokbase.pos.fragment.MyShareFragment;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.UserBean;
import com.common.lib.dialog.MyDialogFragment;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;

import java.util.ArrayList;

public class ObserverActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {

    private ArrayList<MyShareFragment> mFragments;
    private int mCurrentItem;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_observer;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_observer);
        ImageView ivRight = findViewById(R.id.ivRight);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setImageResource(R.drawable.app_edit_black);
        setText(R.id.btnRight, R.string.app_complete);
        setViewsOnClickListener(R.id.btnRight, R.id.ivRight, R.id.llMyShare, R.id.llCollection);
        mCurrentItem = 0;
        mFragments = new ArrayList<>();
        mFragments.add(new MyShareFragment());
        mFragments.add(new CollectionFragment());
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
                resetBtn(findViewById(R.id.llMyShare), findViewById(R.id.llCollection));
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
            case R.id.btnRight:
                setViewGone(v);
                setViewVisible(R.id.ivRight);
                for (MyShareFragment fragment : mFragments) {
                    fragment.setEdit(false);
                }
                break;
            case R.id.ivRight:
                setViewGone(v);
                setViewVisible(R.id.btnRight);
                for (MyShareFragment fragment : mFragments) {
                    fragment.setEdit(true);
                }
                break;
            case R.id.llMyShare:
                if (mCurrentItem == 0) {
                    return;
                }
                mCurrentItem = 0;
                ViewPager viewPager = findViewById(R.id.viewPager);
                viewPager.setCurrentItem(0);
                resetBtn(findViewById(R.id.llMyShare), findViewById(R.id.llCollection));
                break;
            case R.id.llCollection:
                if (mCurrentItem == 1) {
                    return;
                }
                mCurrentItem = 1;
                viewPager = findViewById(R.id.viewPager);
                viewPager.setCurrentItem(1);
                resetBtn(findViewById(R.id.llMyShare), findViewById(R.id.llCollection));
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

    public void showBindGoogleDialog() {
        final MyDialogFragment dialogFragment = new MyDialogFragment(R.layout.layout_show_not_set_google_dialog);
        dialogFragment.setOnMyDialogListener(new MyDialogFragment.OnMyDialogListener() {

            @Override
            public void initView(@Nullable View view) {
                dialogFragment.setDialogViewsOnClickListener(view, R.id.tvBind);
            }

            @Override
            public void onViewClick(int viewId) {
                switch (viewId) {
                    case R.id.tvBind:
                        openActivity(GoogleVerifyActivity.class);
                        break;
                }
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "MyDialogFragment");
    }
}
