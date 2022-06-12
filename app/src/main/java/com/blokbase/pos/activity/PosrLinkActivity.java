package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.PosrLinkBean;
import com.common.lib.constant.Constants;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.common.lib.utils.BaseUtils;


public class PosrLinkActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_posr_link;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        PosrLinkBean bean = (PosrLinkBean) getIntent().getExtras().getSerializable(Constants.BUNDLE_EXTRA);
        setText(R.id.tvLink, bean.getLink());
        setViewsOnClickListener(R.id.tvCopyLink);
    }

    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCopyLink:
                showToast(R.string.app_copy_success);
                BaseUtils.StaticParams.copyData(this, getTextById(R.id.tvLink));
                break;
        }
    }
}
