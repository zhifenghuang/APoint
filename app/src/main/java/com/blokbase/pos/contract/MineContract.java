package com.blokbase.pos.contract;

import com.common.lib.bean.VersionBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface MineContract {
    public interface View extends IView {
        public void checkVersionSuccess(VersionBean bean);

        public void getUserInfoSuccess();
    }

    public interface Presenter extends IPresenter {
        public void checkVersion();

        public void getUserInfo();
    }
}
