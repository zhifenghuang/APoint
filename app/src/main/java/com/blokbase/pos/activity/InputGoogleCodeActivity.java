package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.GoogleVerifyContract;
import com.blokbase.pos.presenter.GoogleVerifyPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.GoogleInfoBean;
import com.common.lib.view.OnInputListener;
import com.common.lib.view.VerifyCodeView;

public class InputGoogleCodeActivity extends BaseActivity<GoogleVerifyContract.Presenter> implements GoogleVerifyContract.View {

    private String mCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_input_google_verify_code;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_google_code);
        VerifyCodeView verifyCodeView = findViewById(R.id.verifyCodeView);

        final TextView tvOk = findViewById(R.id.tvOk);
        tvOk.setBackgroundResource(R.drawable.shape_ededed_9);
        tvOk.setTextColor(ContextCompat.getColor(this, R.color.text_color_4));
        tvOk.setEnabled(false);
        tvOk.setOnClickListener(this);
        verifyCodeView.setOnInputListener(new OnInputListener() {
            @Override
            public void onSuccess(@NonNull String code) {
                mCode = code;
                tvOk.setBackgroundResource(R.drawable.shape_6961f3_9);
                tvOk.setTextColor(ContextCompat.getColor(InputGoogleCodeActivity.this, R.color.text_color_3));
                tvOk.setEnabled(true);
            }

            @Override
            public void onInput() {
                tvOk.setBackgroundResource(R.drawable.shape_ededed_9);
                tvOk.setTextColor(ContextCompat.getColor(InputGoogleCodeActivity.this, R.color.text_color_4));
                tvOk.setEnabled(false);
            }
        });
    }

    @NonNull
    @Override
    protected GoogleVerifyContract.Presenter onCreatePresenter() {
        return new GoogleVerifyPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvOk:
                getPresenter().verifyGoogle(mCode);
                break;
        }
    }

    @Override
    public void getGoogleCodeSuccess(GoogleInfoBean bean) {
    }

    @Override
    public void verifyGoogle(boolean isSuccess) {
        if (isSuccess) {
            openActivity(AddObserverActivity.class);
            showToast(R.string.app_verify_success);
            finish();
        } else {
            showToast(R.string.app_verify_failed);
        }
    }
}
