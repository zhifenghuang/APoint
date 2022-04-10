package com.blokbase.pos.contract;

import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface ObserverLinkContract {

    public interface View extends IView {
        public void deleteObserverSuccess();

        public void updateObserverSuccess(boolean income, boolean account);
    }

    public interface Presenter extends IPresenter {
        public void deleteObserver(String id);

        public void updateObserver(String id, String remark, boolean income, boolean account);
    }
}
