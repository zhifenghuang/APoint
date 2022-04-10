package com.blokbase.pos.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.RegisterContract;
import com.blokbase.pos.presenter.RegisterPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.PicCodeBean;
import com.common.lib.bean.UserBean;
import com.common.lib.constant.Constants;
import com.common.lib.utils.BitmapUtil;
import com.common.lib.utils.LogUtil;

public class RegisterActivity extends BaseActivity<RegisterContract.Presenter> implements RegisterContract.View {

    protected String mKey;
    private boolean mIsAgree;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        mIsAgree = true;
        setTopStatusBarStyle(R.id.llTop);
        setViewsOnClickListener(R.id.ivPicCode, R.id.tvNextStep,
                R.id.llProtocol, R.id.tvProtocol);
        getPresenter().getCaptcha();
        initEditText();
    }

    @NonNull
    @Override
    protected RegisterContract.Presenter onCreatePresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llProtocol:
                mIsAgree = !mIsAgree;
                ((ImageView) ((LinearLayout) v).getChildAt(0)).setImageResource(mIsAgree ? R.drawable.app_checked : R.drawable.app_unchecked);
                break;
            case R.id.tvProtocol:
                openActivity(ProtocolActivity.class);
                break;
            case R.id.ivPicCode:
                getPresenter().getCaptcha();
                break;
            case R.id.tvNextStep:
                if (TextUtils.isEmpty(mKey)) {
                    getPresenter().getCaptcha();
                    return;
                }
                if (!mIsAgree) {
                    showToast(R.string.app_please_agree_user_protocol_first);
                    return;
                }
                String loginAccount = getTextById(R.id.etEmail);
                String inviteCode = getTextById(R.id.etInviteCode);
                String picCode = getTextById(R.id.etVerCode);
                if (TextUtils.isEmpty(loginAccount)) {
                    showToast(R.string.app_please_input_email);
                    return;
                }
                if (TextUtils.isEmpty(picCode)) {
                    showToast(R.string.app_input_pic_code);
                    return;
                }
                getPresenter().register1(loginAccount, inviteCode, picCode, mKey);
                break;
        }
    }

    @Override
    public void getCaptchaSuccess(PicCodeBean bean) {
        mKey = bean.getKey();
        Bitmap bitmap = BitmapUtil.INSTANCE.base64ToBitmap(bean.getBase64().split(",")[1]);
        setBackground(R.id.ivPicCode, bitmap);
    }

    @Override
    public void registerSuccess(UserBean bean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BUNDLE_EXTRA, bean);
        openActivity(SetPasswordActivity.class, bundle);
    }

    private void initEditText() {
        TextView tvNextStep = findViewById(R.id.tvNextStep);
        tvNextStep.setBackgroundResource(R.drawable.shape_ededed_9);
        tvNextStep.setTextColor(ContextCompat.getColor(this, R.color.text_color_4));
        tvNextStep.setEnabled(false);
        initInput(findViewById(R.id.etEmail));
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
        String verCode = getTextById(R.id.etVerCode);
        TextView tvNextStep = findViewById(R.id.tvNextStep);
        LogUtil.LogE(email);
        if (email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
            setViewVisible(R.id.ivCheck);
            if (!TextUtils.isEmpty(verCode)) {
                tvNextStep.setBackgroundResource(R.drawable.shape_6961f3_9);
                tvNextStep.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.text_color_3));
                tvNextStep.setEnabled(true);
            } else {
                tvNextStep.setBackgroundResource(R.drawable.shape_ededed_9);
                tvNextStep.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.text_color_4));
                tvNextStep.setEnabled(false);
            }
        } else {
            setViewInvisible(R.id.ivCheck);
            tvNextStep.setBackgroundResource(R.drawable.shape_ededed_9);
            tvNextStep.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.text_color_4));
            tvNextStep.setEnabled(false);
        }
    }
}
