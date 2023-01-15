package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.DataPreviewContract;
import com.blokbase.pos.presenter.DataPreviewPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.InviteBean;

public class DataPreviewActivity extends BaseActivity<DataPreviewContract.Presenter> implements DataPreviewContract.View {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_preview;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_statistics);
        setTopStatusBarStyle(R.id.topView);
        setBackgroundColor(R.id.topView, R.color.color_ff_ff_ff);
        getPresenter().incomeOverview();
        setViewsOnClickListener(R.id.tvViewDetailFirst, R.id.tvViewDetailSecond, R.id.tvViewDetailThird);
    }

    @NonNull
    @Override
    protected DataPreviewContract.Presenter onCreatePresenter() {
        return new DataPreviewPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvViewDetailFirst:
            case R.id.tvViewDetailSecond:
            case R.id.tvViewDetailThird:
                openActivity(DataDetailActivity.class);
                break;
        }
    }

    @Override
    public void getIncomeOverviewSuccess(InviteBean bean) {
        if (isFinish()) {
            return;
        }
        setText(R.id.tvFirstNum, bean.getLeftAmount());
        setText(R.id.tvSecondNum, bean.getLeft2Amount());
        setText(R.id.tvThirdNum, bean.getRightAmount());
    }
}
