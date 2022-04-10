package com.blokbase.pos.contract;

import com.common.lib.bean.PicCodeBean;
import com.common.lib.bean.UserBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface RegisterContract {
    public interface View extends IView {
        public void getCaptchaSuccess(PicCodeBean bean);

        public void registerSuccess(UserBean bean);
    }

    public interface Presenter extends IPresenter {
        public void getCaptcha();

        public void register1(String loginAccount, String refereeId, String code, String key);
    }
}
