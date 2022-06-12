package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.ProtocolContract;
import com.blokbase.pos.presenter.ProtocolPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.QuestionBean;
import com.common.lib.constant.Constants;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;

public class ProtocolActivity extends BaseActivity<ProtocolContract.Presenter> implements ProtocolContract.View {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_faq_content;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        getPresenter().protocol();
        getProtocolSuccess(DataManager.Companion.getInstance().getProtocol());
    }

    @NonNull
    @Override
    protected ProtocolContract.Presenter onCreatePresenter() {
        return new ProtocolPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getProtocolSuccess(QuestionBean bean) {
        if (bean == null) {
            return;
        }
        setViewGone(R.id.tv);
        setText(R.id.tvTitle, bean.getTitleStr());
        //       setText(R.id.tv, bean.getSubTitleStr());
        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        ((WebView) findViewById(R.id.webView)).
                loadDataWithBaseURL(null, bean.getContentStr(), "text/html", "utf-8", null);
    }
}
