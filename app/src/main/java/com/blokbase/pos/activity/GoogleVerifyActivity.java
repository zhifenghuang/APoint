package com.blokbase.pos.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.common.lib.bean.UserBean;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.BaseUtils;
import com.common.lib.utils.QRCodeUtil;
import com.jakewharton.rxbinding3.widget.RxTextView;

import io.reactivex.functions.Consumer;

public class GoogleVerifyActivity extends BaseActivity<GoogleVerifyContract.Presenter> implements GoogleVerifyContract.View {

    private Bitmap mBmp;
    private String mKey;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_google_verify_machine;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_bind_google_verify_machine);
        getPresenter().getGoogleCode();
        setViewsOnClickListener(R.id.tvCopy, R.id.tvOk);

        final TextView tvOk = findViewById(R.id.tvOk);
        tvOk.setBackgroundResource(R.drawable.shape_ededed_9);
        tvOk.setTextColor(ContextCompat.getColor(GoogleVerifyActivity.this, R.color.text_color_4));
        tvOk.setEnabled(false);
        RxTextView.textChanges(findViewById(R.id.etGoogleCode)).skip(1).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                if (TextUtils.isEmpty(getTextById(R.id.etGoogleCode))) {
                    tvOk.setBackgroundResource(R.drawable.shape_ededed_9);
                    tvOk.setTextColor(ContextCompat.getColor(GoogleVerifyActivity.this, R.color.text_color_4));
                    tvOk.setEnabled(false);
                } else {
                    tvOk.setBackgroundResource(R.drawable.shape_6961f3_9);
                    tvOk.setTextColor(ContextCompat.getColor(GoogleVerifyActivity.this, R.color.text_color_3));
                    tvOk.setEnabled(true);
                }
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
            case R.id.tvCopy:
                if (TextUtils.isEmpty(mKey)) {
                    getPresenter().getGoogleCode();
                    return;
                }
                showToast(R.string.app_copy_success);
                BaseUtils.StaticParams.copyData(this, mKey);
                break;
            case R.id.tvOk:
                getPresenter().verifyGoogle(getTextById(R.id.etGoogleCode));
                break;
        }
    }

    @Override
    public void getGoogleCodeSuccess(GoogleInfoBean bean) {
        mBmp = QRCodeUtil.createQRImage(this, bean.getQrCode(), null);
        mKey = bean.getSecretKey();
        setImage(R.id.ivQrCode, mBmp);
        setText(R.id.tvCode, bean.getSecretKey());
    }

    @Override
    public void verifyGoogle(boolean isSuccess) {
        if (isSuccess) {
            UserBean bean = DataManager.Companion.getInstance().getMyInfo();
            bean.setAuthStatus(true);
            DataManager.Companion.getInstance().saveMyInfo(bean);
            showToast(R.string.app_verify_success);
            finish();
        } else {
            showToast(R.string.app_verify_failed);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBmp != null && !mBmp.isRecycled()) {
            mBmp.recycle();
        }
        mBmp = null;
    }
}
