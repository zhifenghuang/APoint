package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.UserBean;
import com.common.lib.constant.Constants;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.common.lib.utils.BaseUtils;

public class SecurityActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_security_center;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle,R.string.app_edit_profile);
        setViewsOnClickListener(R.id.llPayPsw, R.id.llLoginPsw);
    }

    @Override
    public void onResume() {
        super.onResume();
        UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
        setText(R.id.tvEmail, myInfo.getLoginAccount());
        if (myInfo.getPaymentStatus()) {
            setText(R.id.tvPayPsw, R.string.app_modify);
        }
    }

    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llLoginPsw:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.BUNDLE_EXTRA, DataManager.Companion.getInstance().getMyInfo());
                bundle.putInt(Constants.BUNDLE_EXTRA_2, 2);
                openActivity(SetPasswordActivity.class, bundle);
                break;
            case R.id.llPayPsw:
                UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
                bundle = new Bundle();
                bundle.putSerializable(Constants.BUNDLE_EXTRA, myInfo);
                bundle.putInt(Constants.BUNDLE_EXTRA_2, myInfo.getPaymentStatus() ? 4 : 3);
                openActivity(SetPasswordActivity.class, bundle);
                break;
        }
    }
}
