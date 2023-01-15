package com.blokbase.pos.activity;

import android.Manifest;
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
import com.common.lib.utils.PermissionUtil;
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
        setViewsOnClickListener(R.id.tvCopy, R.id.tvSave);
        mSelectAssets = (AssetsBean) getIntent().getExtras().getSerializable(Constants.BUNDLE_EXTRA);
        setText(R.id.tvTitle, mSelectAssets.getSymbol2());
        String address = mSelectAssets.getAddress();
        mBmp = QRCodeUtil.createQRImage(this, address, null);
        setImage(R.id.ivQrCode, mBmp);
        setText(R.id.tvAddress, address);
        if (mSelectAssets.getSymbol().equalsIgnoreCase("USDT")) {
            setText(R.id.tvTip, R.string.app_address_tip_1);
            setText(R.id.tvNetwork, R.string.app_tron);
        } else if (mSelectAssets.getSymbol().equalsIgnoreCase("INTEGRAL")) {
            setViewGone(R.id.tv1, R.id.tvNetwork);
            setText(R.id.tvTip, R.string.app_address_tip_2);
        }
        setTextViewLinearGradient(R.id.tvNetwork, R.id.tvSave, R.id.tvCopy);
    }

    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSave:
                if (mBmp == null) {
                    return;
                }
                if (!PermissionUtil.INSTANCE.isGrantPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    requestPermission(null, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    return;
                }
                BaseUtils.StaticParams.saveJpegToAlbum(mBmp, this);
                showToast(R.string.app_save_success);
                break;
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
