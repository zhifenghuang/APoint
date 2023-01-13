package com.blokbase.pos.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.jakewharton.rxbinding3.widget.RxTextView;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function4;
import io.reactivex.functions.Function5;

public class ForgetPasswordActivity extends RegisterActivity {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        super.onCreated(savedInstanceState);
        mType = "RESET";
        setText(R.id.tvTitle, R.string.app_forget_psw);
    }

    protected void initEditText() {
        initInput(findViewById(R.id.etEmail));
        TextView tvOk = findViewById(R.id.tvOk);
        tvOk.setBackgroundResource(R.drawable.shape_common_disable_btn_8);
        tvOk.setTextColor(ContextCompat.getColor(this, R.color.text_color_3));
        tvOk.setEnabled(false);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etEmailVerifyCode = findViewById(R.id.etEmailVerifyCode);
        EditText etPsw1 = findViewById(R.id.etPsw1);
        EditText etPsw2 = findViewById(R.id.etPsw2);
        Observable.combineLatest(
                RxTextView.textChanges(etEmail).skip(1),
                RxTextView.textChanges(etEmailVerifyCode).skip(1),
                RxTextView.textChanges(etPsw1).skip(1),
                RxTextView.textChanges(etPsw2).skip(1),
                (Function4<CharSequence, CharSequence, CharSequence, CharSequence, Boolean>) (charSequence, charSequence2, charSequence3, charSequence44) -> {
                    return !TextUtils.isEmpty(getTextById(R.id.etEmail)) &&
                            !TextUtils.isEmpty(getTextById(R.id.etEmailVerifyCode))
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

    public void onOkClick(String email, String psw, String verifyCode) {
        getPresenter().resetLoginPsw(email, psw, psw, verifyCode);
    }

    @Override
    public void registerSuccess() {
        showToast(R.string.app_reset_password_success);
        finish();
    }

}
