package com.blokbase.pos.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.SetPasswordContract;
import com.blokbase.pos.presenter.SetPasswordPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.UserBean;
import com.common.lib.constant.Constants;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.MD5Util;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;

public class SetPasswordActivity extends BaseActivity<SetPasswordContract.Presenter> implements SetPasswordContract.View {

    private UserBean mUser;
    private Timer mTimer;
    private int mTotalTime;
    private TimerTask mTask;
    private boolean mIsPswShow1, mIsPswShow2;
    private int mType;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_password;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setViewsOnClickListener(R.id.tvOk, R.id.tvSendCode, R.id.ivEye1, R.id.ivEye2);
        Bundle bundle = getIntent().getExtras();
        mUser = (UserBean) bundle.getSerializable(Constants.BUNDLE_EXTRA);
        mType = bundle.getInt(Constants.BUNDLE_EXTRA_2, 0);
        if (mType == 2) {
            setText(R.id.tvTitle, R.string.app_modify_login_psw);
        } else if (mType == 3) {
            setText(R.id.tvTitle, R.string.app_set_pay_psw);
        } else if (mType == 4) {
            setText(R.id.tvTitle, R.string.app_modify_pay_psw);
        }
        initEditText();
        mIsPswShow1 = false;
        mIsPswShow2 = false;
    }

    @NonNull
    @Override
    protected SetPasswordContract.Presenter onCreatePresenter() {
        return new SetPasswordPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSendCode:
                getPresenter().sendEmail(mUser.getLoginAccount(), mType == 0 ? "REGISTER" : "RESET");
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
                String verifyCode = getTextById(R.id.etEmailVerifyCode);
                String psw1 = getTextById(R.id.etPsw1);
                String psw2 = getTextById(R.id.etPsw2);
                if (TextUtils.isEmpty(verifyCode)) {
                    showToast(R.string.app_please_input_email_verify_code);
                    return;
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
                if (mType == 2) {
                    getPresenter().modifyPsw(verifyCode, psw, psw, "LOGIN");
                } else if (mType == 3 || mType == 4) {
                    getPresenter().modifyPsw(verifyCode, psw, psw, "PAYMENT");
                }
                break;
        }
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

    public void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void setPasswordSuccess() {
        if (mType == 3) {
            UserBean myInfo = DataManager.Companion.getInstance().getMyInfo();
            myInfo.setPaymentStatus(true);
            DataManager.Companion.getInstance().saveMyInfo(myInfo);
            showToast(R.string.app_set_psw_success);
        } else {
            showToast(mType == 2 ? R.string.app_modify_psw_success_please_relogin :
                    R.string.app_modify_psw_success);
        }
        if (mType == 2) {
            finishAllActivity();
            DataManager.Companion.getInstance().logout();
            openActivity(LoginActivity.class);
        } else {
            finish();
        }

    }

    private void initEditText() {
        TextView tvOk = findViewById(R.id.tvOk);
        tvOk.setEnabled(false);
        EditText etEmailVerifyCode = findViewById(R.id.etEmailVerifyCode);
        EditText etPsw1 = findViewById(R.id.etPsw1);
        EditText etPsw2 = findViewById(R.id.etPsw2);
        tvOk.setBackgroundResource(R.drawable.shape_common_disable_btn_8);
        Observable.combineLatest(RxTextView.textChanges(etEmailVerifyCode).skip(1),
                RxTextView.textChanges(etPsw1).skip(1),
                RxTextView.textChanges(etPsw2).skip(1),
                (Function3<CharSequence, CharSequence, CharSequence, Boolean>) (charSequence, charSequence2, charSequence3) -> {
                    return !TextUtils.isEmpty(getTextById(R.id.etEmailVerifyCode))
                            && !TextUtils.isEmpty(getTextById(R.id.etPsw1))
                            && !TextUtils.isEmpty(getTextById(R.id.etPsw2));
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    tvOk.setEnabled(true);
                    tvOk.setBackgroundResource(R.drawable.shape_common_btn_8);
                } else {
                    tvOk.setEnabled(false);
                    tvOk.setBackgroundResource(R.drawable.shape_common_disable_btn_8);
                }
            }
        });
    }
}
