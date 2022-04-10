package com.blokbase.pos.contract;

import com.common.lib.bean.PicCodeBean;
import com.common.lib.bean.UserBean;
import com.common.lib.bean.VersionBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface LoginContract {
    public interface View extends IView {
        public void getCaptchaSuccess(PicCodeBean bean);

        public void loginSuccess();

        public void checkVersionSuccess(VersionBean bean);
    }

    public interface Presenter extends IPresenter {
        public void getCaptcha();

        public void login(String loginAccount, String loginPassword, String code, String key);

        public void checkVersion();
    }
}
