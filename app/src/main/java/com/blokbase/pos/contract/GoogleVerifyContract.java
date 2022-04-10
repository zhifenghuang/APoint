package com.blokbase.pos.contract;

import com.common.lib.bean.GoogleInfoBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface GoogleVerifyContract {
    public interface View extends IView {
        public void getGoogleCodeSuccess(GoogleInfoBean bean);

        public void verifyGoogle(boolean isSuccess);
    }

    public interface Presenter extends IPresenter {
        public void getGoogleCode();

        public void verifyGoogle(String code);
    }
}
