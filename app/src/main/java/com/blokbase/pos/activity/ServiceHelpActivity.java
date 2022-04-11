package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.ServiceHelpContract;
import com.blokbase.pos.presenter.ServiceHelpPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.QuestionBean;

public class ServiceHelpActivity extends BaseActivity<ServiceHelpContract.Presenter> implements ServiceHelpContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_service_help;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(R.id.llTop);
        setViewsOnClickListener(R.id.tvTelegram, R.id.tvNuggetsTutorial);
        getPresenter().serviceHelp();
    }

    @NonNull
    @Override
    protected ServiceHelpContract.Presenter onCreatePresenter() {
        return new ServiceHelpPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTelegram:
                break;
            case R.id.tvNuggetsTutorial:
                openActivity(FAQActivity.class);
                break;
        }
    }

    @Override
    public void getServiceHelpSuccess(QuestionBean poster) {

    }
}
