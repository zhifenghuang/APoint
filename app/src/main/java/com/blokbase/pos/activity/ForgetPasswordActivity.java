package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import com.blokbase.pos.R;
import com.common.lib.bean.UserBean;
import com.common.lib.constant.Constants;

public class ForgetPasswordActivity extends RegisterActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvNextStep) {
            UserBean user = new UserBean();
            user.setHash(mKey);
            user.setCode(getTextById(R.id.etVerCode));
            user.setLoginAccount(getTextById(R.id.etEmail));
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.BUNDLE_EXTRA, user);
            bundle.putInt(Constants.BUNDLE_EXTRA_2, 1);
            openActivity(SetPasswordActivity.class, bundle);
        } else {
            super.onClick(v);
        }
    }
}
