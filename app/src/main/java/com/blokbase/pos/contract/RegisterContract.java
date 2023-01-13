package com.blokbase.pos.contract;

import com.common.lib.bean.UserBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface RegisterContract {
    public interface View extends IView {

        public void sendEmailSuccess();

        public void sendEmailFailed();

        public void registerSuccess();
    }

    public interface Presenter extends IPresenter {

        public void sendEmail(String email, String type);

        public void register(String loginAccount, String loginPassword, String refereeId, String verifyCode);

        public void resetLoginPsw(String loginAccount, String loginPassword, String reLoginPassword,String verifyCode);
    }
}
