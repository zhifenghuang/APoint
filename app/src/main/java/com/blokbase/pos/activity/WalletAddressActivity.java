package com.blokbase.pos.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.AssetsBean;
import com.common.lib.constant.Constants;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.common.lib.utils.BaseUtils;
import com.common.lib.utils.QRCodeUtil;

public class WalletAddressActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {

    private AssetsBean mSelectAssets;
    private Bitmap mBmp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_address;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setViewsOnClickListener(R.id.tvCopy);
        mSelectAssets = (AssetsBean) getIntent().getExtras().getSerializable(Constants.BUNDLE_EXTRA);
        setText(R.id.tvTitle, mSelectAssets.getSymbol());
        mBmp = QRCodeUtil.createQRImage(this, mSelectAssets.getAddress(), null);
        setImage(R.id.ivQrCode, mBmp);
        setText(R.id.tvAddress, mSelectAssets.getAddress());
    }

    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvCopy:
                showToast(R.string.app_copy_success);
                BaseUtils.StaticParams.copyData(this, mSelectAssets.getAddress());
                break;
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
