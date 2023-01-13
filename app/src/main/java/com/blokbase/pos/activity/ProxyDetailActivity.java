package com.blokbase.pos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.ProxyApplyContract;
import com.blokbase.pos.dialog.InputDialog;
import com.blokbase.pos.presenter.ProxyApplyPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.AgentBean;
import com.common.lib.bean.DistrictBean;
import com.common.lib.bean.UserBean;
import com.common.lib.constant.Constants;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.tencent.bugly.crashreport.biz.UserInfoBean;

import java.util.ArrayList;

public class ProxyDetailActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_proxy_detail;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_proxy_detail);
        UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
        if (myInfo.getAgent() != null) {
            setText(R.id.tv1, getString(R.string.app_you_are) + myInfo.getAgent().getNameStr());
        }
        setText(R.id.tv2, getString(R.string.app_your_proxy_distinct) + myInfo.getAgentTitle());
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
