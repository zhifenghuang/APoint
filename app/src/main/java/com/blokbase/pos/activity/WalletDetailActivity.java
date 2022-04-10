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
import com.blokbase.pos.contract.WalletDetailContract;
import com.blokbase.pos.fragment.TransferListFragment;
import com.blokbase.pos.presenter.WalletDetailPresenter;
import com.blokbase.pos.util.Utils;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.AssetsBean;
import com.common.lib.constant.Constants;
import com.common.lib.manager.DataManager;

import java.math.BigDecimal;
import java.util.ArrayList;

public class WalletDetailActivity extends BaseActivity<WalletDetailContract.Presenter> implements WalletDetailContract.View {

    private AssetsBean mSelectAssets;
    private ArrayList<TransferListFragment> mFragments;
    private int mCurrentItem;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_detail;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setViewsOnClickListener(R.id.llBillDetail, R.id.llChargeRecord, R.id.llFreeze);
        mSelectAssets = (AssetsBean) getIntent().getExtras().getSerializable(Constants.BUNDLE_EXTRA);
        setText(R.id.tvTitle, mSelectAssets.getSymbol());
        resetBalanceUI();

        mCurrentItem = 0;
        mFragments = new ArrayList<>();
        mFragments.add(TransferListFragment.getInstance(mSelectAssets.getSymbol(), 0, 0));
        mFragments.add(TransferListFragment.getInstance(mSelectAssets.getSymbol(), 1, 0));
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
                resetBtn(findViewById(R.id.llBillDetail), findViewById(R.id.llChargeRecord));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(mCurrentItem);
    }

    @NonNull
    @Override
    protected WalletDetailContract.Presenter onCreatePresenter() {
        return new WalletDetailPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().assetsList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llBillDetail:
                if (mCurrentItem == 0) {
                    return;
                }
                mCurrentItem = 0;
                ViewPager viewPager = findViewById(R.id.viewPager);
                viewPager.setCurrentItem(0);
                resetBtn(findViewById(R.id.llBillDetail), findViewById(R.id.llChargeRecord));
                break;
            case R.id.llChargeRecord:
                if (mCurrentItem == 1) {
                    return;
                }
                mCurrentItem = 1;
                viewPager = findViewById(R.id.viewPager);
                viewPager.setCurrentItem(1);
                resetBtn(findViewById(R.id.llBillDetail), findViewById(R.id.llChargeRecord));
                break;
            case R.id.llFreeze:
                openActivity(FreezeListActivity.class);
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

    private void resetBalanceUI() {
        String symbol = mSelectAssets.getSymbol();
        setText(R.id.tvBalance, Utils.removeZero(mSelectAssets.getBalance()));
        String toUsdt = mSelectAssets.getBalance();
        BigDecimal balance = new BigDecimal(Utils.removeZero(mSelectAssets.getBalance()));
        if (!balance.equals(BigDecimal.ZERO)) {
            if (symbol.equalsIgnoreCase("UTG")) {
                String price = DataManager.Companion.getInstance().getUtgPrice();
                toUsdt = balance.multiply(new BigDecimal(price)).toString();
            }
        }
        setText(R.id.tvToUsdt, "≈ $" + Utils.removeZero(toUsdt));
        setText(R.id.tvAvailable, Utils.removeZero(mSelectAssets.getBalance()));
        setText(R.id.tvFreeze, Utils.removeZero(mSelectAssets.getFreeze()));
    }

    @Override
    public void getAssetsListSuccess(ArrayList<AssetsBean> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        DataManager.Companion.getInstance().saveAssets(list);
        String symbol = mSelectAssets.getSymbol();
        for (AssetsBean bean : list) {
            if (bean.getSymbol().equals(symbol)) {
                mSelectAssets = bean;
                resetBalanceUI();
                break;
            }
        }
    }
}
