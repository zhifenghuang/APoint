package com.blokbase.pos.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.blokbase.pos.R;
import com.blokbase.pos.contract.AboutUsContract;
import com.blokbase.pos.presenter.AboutUsPresenter;
import com.common.lib.activity.BaseActivity;
import com.common.lib.bean.QuestionBean;
import com.common.lib.manager.DataManager;

public class AboutUsActivity extends BaseActivity<AboutUsContract.Presenter> implements AboutUsContract.View {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setTopStatusBarStyle(R.id.llTop);
        setTextColor(R.id.tvTitle, R.color.text_color_3);
        setImage(R.id.ivLeft, R.drawable.app_white_back);
        setText(R.id.tvTitle, R.string.app_about_us);
        getPresenter().aboutUs();
        getAboutUsSuccess(DataManager.Companion.getInstance().getAboutUs());
        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @NonNull
    @Override
    protected AboutUsContract.Presenter onCreatePresenter() {
        return new AboutUsPresenter(this);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void getAboutUsSuccess(QuestionBean bean) {
        if (isFinish() || bean == null) {
            return;
        }
        setText(R.id.tv1, bean.getTitleStr());
        setHtml(R.id.tv2, bean.getSubTitleStr());
        ((WebView) findViewById(R.id.webView)).
                loadDataWithBaseURL(null, bean.getContentStr(), "text/html", "utf-8", null);
    }
}
