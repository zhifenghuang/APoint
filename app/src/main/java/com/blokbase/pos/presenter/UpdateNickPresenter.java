package com.blokbase.pos.presenter;

import com.blokbase.pos.contract.UpdateNickContract;
import com.common.lib.mvp.BasePresenter;
import com.common.lib.network.HttpListener;
import com.common.lib.network.HttpMethods;
import com.common.lib.network.HttpObserver;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UpdateNickPresenter extends BasePresenter<UpdateNickContract.View> implements UpdateNickContract.Presenter {

    public UpdateNickPresenter(@NotNull UpdateNickContract.View rootView) {
        super(rootView);
    }

    @Override
    public void updateNick(String nick) {
        HttpMethods.Companion.getInstance().saveNick(nick, new HttpObserver(getRootView(), new HttpListener<Object>() {
                    @Override
                    public void onSuccess(@Nullable int totalCount, @Nullable Object object) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().updateNickSuccess(nick);
                    }

                    @Override
                    public void dataError(@Nullable int code, @Nullable String msg) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().showErrorDialog(code, msg);
                    }

                    @Override
                    public void connectError(@Nullable Throwable e) {
                        if (getRootView() == null) {
                            return;
                        }
                        getRootView().showErrorDialog(-1, "");
                    }
                }, getCompositeDisposable()));
    }
}
