package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.common.lib.constant.Constants;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.blokbase.pos.R;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.QuestionBean;

public class FAQContentActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_faq_content;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_faq);
        QuestionBean bean = (QuestionBean) getIntent().getExtras().getSerializable(Constants.BUNDLE_EXTRA);
        setText(R.id.tv, bean.getTitleStr());

        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        ((WebView) findViewById(R.id.webView)).
                loadDataWithBaseURL(null, bean.getContentStr(), "text/html", "utf-8", null);
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
