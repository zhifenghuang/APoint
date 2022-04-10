package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.AssetsDetailContract;
import com.common.lib.mvp.BasePresenter;

import org.jetbrains.annotations.NotNull;

public class AssetsDetailPresenter extends BasePresenter<AssetsDetailContract.View> implements AssetsDetailContract.Presenter {

    public AssetsDetailPresenter(@NotNull AssetsDetailContract.View rootView) {
        super(rootView);
    }

}
