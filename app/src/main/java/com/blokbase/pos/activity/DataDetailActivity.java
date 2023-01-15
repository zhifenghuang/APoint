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

public class DataDetailActivity extends BaseActivity<DataPreviewContract.Presenter> implements DataPreviewContract.View {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_detail;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_data_detail);
    }

    @NonNull
    @Override
    protected DataPreviewContract.Presenter onCreatePresenter() {
        return new DataPreviewPresenter(this);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void getIncomeOverviewSuccess(InviteBean bean) {

    }
}
