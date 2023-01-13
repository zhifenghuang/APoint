package com.blokbase.pos.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.BuildConfig;
import com.blokbase.pos.R;
import com.blokbase.pos.contract.LoginContract;
import com.blokbase.pos.presenter.LoginPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.PicCodeBean;
import com.common.lib.bean.VersionBean;
import com.common.lib.dialog.AppUpgradeDialog;
import com.common.lib.utils.BitmapUtil;
import com.common.lib.utils.MD5Util;

public class LoginActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View {

    private boolean mIsPswShow;
    private String mKey;
    private AppUpgradeDialog mAppUpgradeDialog;
    private boolean mIsActivityPause;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setViewsOnClickListener(R.id.ivPicCode, R.id.tvForgetPsw, R.id.tvLogin, R.id.llRegister);
        initEditText();
        mIsPswShow = false;
        getPresenter().getCaptcha();
        getPresenter().checkVersion();
    }

    @NonNull
    @Override
    protected LoginContract.Presenter onCreatePresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPicCode:
                getPresenter().getCaptcha();
                break;
            case R.id.tvForgetPsw:
                openActivity(ForgetPasswordActivity.class);
                break;
            case R.id.llRegister:
                openActivity(RegisterActivity.class);
                break;
            case R.id.tvLogin:
                if (TextUtils.isEmpty(mKey)) {
                    getPresenter().getCaptcha();
                    return;
                }
                String loginAccount = getTextById(R.id.etEmail);
                String loginPsw = getTextById(R.id.etPassword);
                String picCode = getTextById(R.id.etVerCode);
                if (TextUtils.isEmpty(loginAccount)) {
                    showToast(R.string.app_please_input_email);
                    return;
                }
                if (TextUtils.isEmpty(loginPsw)) {
                    showToast(R.string.app_please_input_login_psw);
                    return;
                }
                if (TextUtils.isEmpty(picCode)) {
                    showToast(R.string.app_input_pic_code);
                    return;
                }
                getPresenter().login(loginAccount, MD5Util.INSTANCE.getMd5(loginPsw), picCode, mKey);
                break;
        }
    }


    private void initEditText() {
        TextView tvLogin = findViewById(R.id.tvLogin);
        tvLogin.setBackgroundResource(R.drawable.shape_common_disable_btn_8);
        tvLogin.setTextColor(ContextCompat.getColor(this, R.color.text_color_3));
        tvLogin.setEnabled(false);
        initInput(findViewById(R.id.etEmail));
        initInput(findViewById(R.id.etPassword));
        initInput(findViewById(R.id.etVerCode));
    }

    private void initInput(EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void checkInput() {
        String email = getTextById(R.id.etEmail);
        String password = getTextById(R.id.etPassword);
        String verCode = getTextById(R.id.etVerCode);
        TextView tvLogin = findViewById(R.id.tvLogin);
        if (email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
            if (!TextUtils.isEmpty(verCode) && !TextUtils.isEmpty(password)) {
                tvLogin.setBackgroundResource(R.drawable.shape_common_btn_8);
                tvLogin.setEnabled(true);
            } else {
                tvLogin.setBackgroundResource(R.drawable.shape_common_disable_btn_8);
                tvLogin.setEnabled(false);
            }
        } else {
            tvLogin.setBackgroundResource(R.drawable.shape_common_disable_btn_8);
            tvLogin.setEnabled(false);
        }
    }

    @Override
    public void getCaptchaSuccess(PicCodeBean bean) {
        mKey = bean.getKey();
        Bitmap bitmap = BitmapUtil.INSTANCE.base64ToBitmap(bean.getBase64().split(",")[1]);
        setBackground(R.id.ivPicCode, bitmap);
    }

    @Override
    public void loginSuccess() {
        finishAllActivity();
        openActivity(MainActivity.class);
    }

    @Override
    public void checkVersionSuccess(VersionBean bean) {
        if (bean.getVersionCode() > BuildConfig.VERSION_CODE && !mIsActivityPause) {
            mAppUpgradeDialog = new AppUpgradeDialog(this, bean);
            mAppUpgradeDialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsActivityPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsActivityPause = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAppUpgradeDialog != null) {
            mAppUpgradeDialog.dismiss();
        }
        mAppUpgradeDialog = null;
    }
}
