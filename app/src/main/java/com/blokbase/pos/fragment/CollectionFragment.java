package com.blokbase.pos.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;


import com.blokbase.pos.activity.AddCollectionActivity;
import com.blokbase.pos.activity.AddObserverActivity;
import com.blokbase.pos.activity.InputGoogleCodeActivity;
import com.blokbase.pos.activity.ObserverActivity;
import com.common.lib.bean.UserBean;
import com.common.lib.constant.Constants;
import com.common.lib.manager.DataManager;

public class CollectionFragment extends MyShareFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collection;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mType = "COLLECT";
        setViewsOnClickListener(R.id.tvCreateCollection);
        initView(view);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCreateCollection:
                openActivity(AddCollectionActivity.class);
                break;
        }
    }
}