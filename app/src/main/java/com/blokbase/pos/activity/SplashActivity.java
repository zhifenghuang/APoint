package com.blokbase.pos.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.common.lib.activity.BaseActivity;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.common.lib.utils.PrefUtil;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        next();
    }

    private void next() {
        findViewById(R.id.iv).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PrefUtil.getBoolean(SplashActivity.this, "is_guide_show", false)) {
                    openActivity(DataManager.Companion.getInstance().getMyInfo() == null ?
                            LoginActivity.class : MainActivity.class);
                } else {
                    openActivity(GuideActivity.class);
                }
                finish();
            }
        }, 1500);
    }

    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }
}
