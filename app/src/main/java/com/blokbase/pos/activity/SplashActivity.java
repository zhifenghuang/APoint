package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.common.lib.activity.BaseActivity;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;

public class SplashActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
//        getWindow().getDecorView().setBackgroundResource(DataManager.Companion.getInstance().getLanguage() == 0 ?
//                R.drawable.app_splash_en : R.drawable.app_splash);
//        getSystemInfo(false);

//
//        boolean isAgree = PrefUtil.getBoolean(this, "is_agree", false);
//        if (isAgree) {
            next();
//        } else {
//            showAgreementDialog();
//        }
    }

    private void next() {
//                long delayTime = 2000;
//        String splashUrl = DataManager.Companion.getInstance().getSplashUrl();
//        if (!TextUtils.isEmpty(splashUrl) && splashUrl.startsWith("http")) {
//            BaseUtils.StaticParams.loadImage(this, 0, splashUrl, findViewById(R.id.iv));
//        } else {
//            delayTime = 2000;
//        }
        findViewById(R.id.iv).postDelayed(new Runnable() {
            @Override
            public void run() {
                openActivity(DataManager.Companion.getInstance().getMyInfo() == null ?
                        LoginActivity.class : MainActivity.class);
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
