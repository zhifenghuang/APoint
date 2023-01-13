package com.blokbase.pos.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.ContractServiceContract;
import com.blokbase.pos.presenter.ContractServicePresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.ContractUsBean;
import com.common.lib.bean.NoticeBean;
import com.common.lib.manager.DataManager;
import com.common.lib.utils.BaseUtils;

public class ContactServiceActivity extends BaseActivity<ContractServiceContract.Presenter> implements ContractServiceContract.View {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_contact_service;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_contact_service);
        //setViewsOnClickListener(R.id.tvEmail, R.id.tvEmail2);

        getPresenter().getContract();
        getContractSuccess(DataManager.Companion.getInstance().getContractUsBean());
    }

    @NonNull
    @Override
    protected ContractServiceContract.Presenter onCreatePresenter() {
        return new ContractServicePresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tvEmail:
//            case R.id.tvEmail2:
//                showToast(R.string.app_copy_success);
//                BaseUtils.StaticParams.copyData(this, ((TextView) v).getText().toString());
//                break;
        }
    }

    @Override
    public void getContractSuccess(NoticeBean bean) {
        if (isFinish() || bean == null) {
            return;
        }
        if (!TextUtils.isEmpty(bean.getContentStr())) {
            setHtml(R.id.tvContent, bean.getContentStr());
        }
    }
}
