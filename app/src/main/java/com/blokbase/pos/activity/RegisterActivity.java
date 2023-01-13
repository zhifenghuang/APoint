package com.blokbase.pos.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.common.lib.utils.MD5Util;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function4;
import io.reactivex.functions.Function5;

public class RegisterActivity extends BaseActivity<RegisterContract.Presenter> implements RegisterContract.View {

    protected String mType;

    private boolean mIsAgree;
    private Timer mTimer;
    private int mTotalTime;
    private TimerTask mTask;
    private boolean mIsPswShow1, mIsPswShow2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        mIsAgree = true;
        mType = "REGISTER";
        setViewsOnClickListener(R.id.llProtocol, R.id.tvSendCode, R.id.ivEye1, R.id.ivEye2, R.id.tvProtocol, R.id.tvOk);
        setText(R.id.tvTitle, R.string.app_create_account);
        initEditText();
        mIsPswShow1 = false;
        mIsPswShow2 = false;
    }

    @NonNull
    @Override
    protected RegisterContract.Presenter onCreatePresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSendCode:
                String email = getTextById(R.id.etEmail);
                if (TextUtils.isEmpty(email)) {
                    showToast(R.string.app_please_input_email);
                    return;
                }
                getPresenter().sendEmail(email, mType);
                break;
            case R.id.llProtocol:
                mIsAgree = !mIsAgree;
                ((ImageView) ((LinearLayout) v).getChildAt(0)).setImageResource(mIsAgree ? R.drawable.app_selected : R.drawable.app_unselected);
                break;
            case R.id.tvProtocol:
                openActivity(ProtocolActivity.class);
                break;
            case R.id.ivEye1:
                EditText etPsw1 = findViewById(R.id.etPsw1);
                mIsPswShow1 = !mIsPswShow1;
                if (mIsPswShow1) {
                    setImage(R.id.ivEye1, R.drawable.app_eye_open);
                    etPsw1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    setImage(R.id.ivEye1, R.drawable.app_eye_close);
                    etPsw1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etPsw1.setSelection(etPsw1.getText().toString().length());
                break;
            case R.id.ivEye2:
                EditText etPsw2 = findViewById(R.id.etPsw2);
                mIsPswShow2 = !mIsPswShow2;
                if (mIsPswShow2) {
                    setImage(R.id.ivEye2, R.drawable.app_eye_open);
                    etPsw2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    setImage(R.id.ivEye2, R.drawable.app_eye_close);
                    etPsw2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                etPsw2.setSelection(etPsw2.getText().toString().length());
                break;
            case R.id.tvOk:
                if (!mIsAgree) {
                    showToast(R.string.app_please_agree_user_protocol_first);
                    return;
                }
                email = getTextById(R.id.etEmail);
                String verifyCode = getTextById(R.id.etEmailVerifyCode);
                String psw1 = getTextById(R.id.etPsw1);
                String psw2 = getTextById(R.id.etPsw2);
                if (TextUtils.isEmpty(email)) {
                    showToast(R.string.app_please_input_email);
                    return;
                }
                if (TextUtils.isEmpty(verifyCode)) {
                    showToast(R.string.app_please_input_email_verify_code);
                    return;
                }
                if (mType.equals("REGISTER")) {
                    String inviteCode = getTextById(R.id.etInviteCode);
                    if (TextUtils.isEmpty(inviteCode)) {
                        showToast(R.string.app_please_input_invite_code);
                        return;
                    }
                }
                if (TextUtils.isEmpty(psw1)) {
                    showToast(R.string.app_please_input_login_psw);
                    return;
                }
                if (TextUtils.isEmpty(psw2)) {
                    showToast(R.string.app_please_input_psw_again);
                    return;
                }
                if (!psw1.equals(psw2)) {
                    showToast(R.string.app_psw_not_equal_sure_psw);
                    return;
                }
                String psw = MD5Util.INSTANCE.getMd5(psw1);
                onOkClick(email, psw, verifyCode);
                break;
        }
    }

    public void onOkClick(String email, String psw, String verifyCode) {
        String inviteCode = getTextById(R.id.etInviteCode);
        getPresenter().register(email, psw, inviteCode, verifyCode);
    }


    @Override
    public void sendEmailSuccess() {
        mTotalTime = 60;
        mTimer = new Timer();
        initTimerTask();
        mTimer.schedule(mTask, 1000, 1000);
    }

    @Override
    public void sendEmailFailed() {
        TextView tvSendCode = findViewById(R.id.tvSendCode);
        tvSendCode.setText(getString(R.string.app_resend));
        tvSendCode.setEnabled(true);
    }

    @Override
    public void registerSuccess() {
        showToast(R.string.app_register_success);
        finish();
    }

    protected void initInput(EditText et) {
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
        if (email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
            setViewVisible(R.id.ivCheck);
        } else {
            setViewInvisible(R.id.ivCheck);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    protected void initEditText() {
        initInput(findViewById(R.id.etEmail));
        TextView tvOk = findViewById(R.id.tvOk);
        tvOk.setBackgroundResource(R.drawable.shape_common_disable_btn_8);
        tvOk.setTextColor(ContextCompat.getColor(this, R.color.text_color_3));
        tvOk.setEnabled(false);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etEmailVerifyCode = findViewById(R.id.etEmailVerifyCode);
        EditText etInviteCode = findViewById(R.id.etInviteCode);
        EditText etPsw1 = findViewById(R.id.etPsw1);
        EditText etPsw2 = findViewById(R.id.etPsw2);
        Observable.combineLatest(
                RxTextView.textChanges(etEmail).skip(1),
                RxTextView.textChanges(etEmailVerifyCode).skip(1),
                RxTextView.textChanges(etInviteCode).skip(1),
                RxTextView.textChanges(etPsw1).skip(1),
                RxTextView.textChanges(etPsw2).skip(1),
                (Function5<CharSequence, CharSequence, CharSequence, CharSequence, CharSequence, Boolean>) (charSequence, charSequence2, charSequence3, charSequence4, charSequence5) -> {
                    return !TextUtils.isEmpty(getTextById(R.id.etEmail)) &&
                            !TextUtils.isEmpty(getTextById(R.id.etEmailVerifyCode)) &&
                            !TextUtils.isEmpty(getTextById(R.id.etInviteCode))
                            && !TextUtils.isEmpty(getTextById(R.id.etPsw1))
                            && !TextUtils.isEmpty(getTextById(R.id.etPsw2));
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    tvOk.setBackgroundResource(R.drawable.shape_common_btn_8);
                    tvOk.setEnabled(true);
                } else {
                    tvOk.setBackgroundResource(R.drawable.shape_common_disable_btn_8);
                    tvOk.setEnabled(false);
                }
            }
        });
    }

    private void initTimerTask() {
        mTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinish()) {
                            return;
                        }
                        --mTotalTime;
                        TextView tvSendCode = findViewById(R.id.tvSendCode);
                        if (mTotalTime <= 0) {
                            tvSendCode.setText(getString(R.string.app_resend));
                            tvSendCode.setEnabled(true);
                            mTimer.cancel();
                            mTimer = null;
                        } else {
                            tvSendCode.setText(getString(R.string.app_resend_xxx, String.valueOf(mTotalTime)));
                            tvSendCode.setEnabled(false);
                        }
                    }
                });
            }
        };
    }

}
