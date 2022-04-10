package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.common.lib.activity.BaseActivity;
import com.common.lib.manager.DataManager;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;

public class LanguageActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_language;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        setText(R.id.tvTitle, R.string.app_language);
        setViewsOnClickListener(R.id.llLanguage0, R.id.llLanguage1);
        resetUI();
    }

    @NonNull
    @Override
    protected EmptyContract.Presenter onCreatePresenter() {
        return new EmptyPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llLanguage0:
                DataManager.Companion.getInstance().saveLanguage(0);
                resetUI();
                break;
            case R.id.llLanguage1:
                DataManager.Companion.getInstance().saveLanguage(1);
                resetUI();
                break;
        }
    }

    private void resetUI() {
        int language = DataManager.Companion.getInstance().getLanguage();
        for (int i = 0; i < 2; ++i) {
            LinearLayout ll = findViewById(getResources().getIdentifier("llLanguage" + i, "id", getPackageName()));
            ll.getChildAt(1).setVisibility(language == i ? View.VISIBLE : View.GONE);
        }
    }
}
