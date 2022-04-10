package com.blokbase.pos.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.blokbase.pos.util.Utils;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.TransferBean;
import com.common.lib.constant.Constants;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;

public class AssetsRecordDetailActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_assets_record_detail;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_detail);
        TransferBean bean = (TransferBean) getIntent().getExtras().getSerializable(Constants.BUNDLE_EXTRA);
        setText(R.id.tvType, bean.getOrigin());
        if (bean.getStatus() == 20) {
            setImage(R.id.iv, R.drawable.app_failed);
        } else if (bean.getStatus() == 30) {
            setImage(R.id.iv, R.drawable.app_complete);
        } else {
            setImage(R.id.iv, R.drawable.app_sending);
        }
        boolean isOut = bean.getDirection().equalsIgnoreCase("OUT");
        setText(R.id.tvAmount, (isOut ? "-" : "+")
                + Utils.removeZero(bean.getAmount()) + " " + bean.getSymbol());
        setText(R.id.tvTime, bean.getCreateTime().substring(0, 16));
        try {
            int strId = getResources().getIdentifier("app_status_" + bean.getStatus(), "string", getPackageName());
            setText(R.id.tvStatus, strId);
        } catch (Exception e) {

        }

        setText(R.id.tvFee, Utils.removeZero(bean.getFee()) + " " + bean.getSymbol());
        if (TextUtils.isEmpty(bean.getToAddress()) ||
                bean.getToAddress().equalsIgnoreCase("system")) {
            setViewGone(R.id.llAddress);
        } else {
            setText(R.id.tvAddress, bean.getToAddress());
        }
        if (TextUtils.isEmpty(bean.getHash())) {
            if (bean.getType() == 6 && !isOut
                    && Utils.removeZero(bean.getAmount()).equals("0")) {
                setText(R.id.tv, R.string.app_remark_1);
                setText(R.id.tvTxId, getString(R.string.app_freeze_xxx,
                        Utils.removeZero(bean.getFreeze()) + " " + bean.getSymbol()));
            } else {
                setViewGone(R.id.llTxId);
            }
        } else {
            setText(R.id.tvTxId, bean.getHash());
        }
    }

    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {
    }
}
