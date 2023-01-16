package com.blokbase.pos.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blokbase.pos.R;
import com.blokbase.pos.adapter.GuideBannerViewHolder;
import com.common.lib.activity.BaseActivity;
import com.common.lib.mvp.contract.EmptyContract;
import com.common.lib.mvp.presenter.EmptyPresenter;
import com.common.lib.utils.PrefUtil;
import com.common.lib.view.banner.BannerView;
import com.common.lib.view.banner.HolderCreator;

import java.util.ArrayList;

public class GuideActivity extends BaseActivity<EmptyContract.Presenter> implements EmptyContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onCreated(@Nullable Bundle savedInstanceState) {
        PrefUtil.putBoolean(this, "is_guide_show", true);
        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.drawable.app_guide_1);
        list.add(R.drawable.app_guide_2);
        list.add(R.drawable.app_guide_3);
        showBanners(list);
    }

    public void showBanners(ArrayList<Integer> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        BannerView bannerView = findViewById(R.id.bannerView);
        bannerView.setIndicatorVisible(false);
        bannerView.setPages(list, new HolderCreator<GuideBannerViewHolder>() {
            @Override
            public GuideBannerViewHolder createViewHolder() {
                return new GuideBannerViewHolder();
            }
        });
        bannerView.start();
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
