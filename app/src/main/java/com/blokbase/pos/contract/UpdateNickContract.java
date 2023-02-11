package com.blokbase.pos.contract;

import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface UpdateNickContract {
    public interface View extends IView {
        public void updateNickSuccess(String nick);
    }

    public interface Presenter extends IPresenter {

        public void updateNick(String nick);
    }
}
