package com.blokbase.pos.contract;

import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface SetPasswordContract {

    public interface View extends IView {
        public void sendEmailSuccess();

        public void sendEmailFailed();

        public void setPasswordSuccess();
    }

    public interface Presenter extends IPresenter {
        public void sendEmail(String email,String type);

        public void register2(String hash,
                              String loginPassword,
                              String reLoginPassword,
                              String verifyCode);

        public void resetLoginPsw(
                String loginAccount,
                String loginPassword,
                String reLoginPassword,
                String verifyCode,
                String code,
                String key);

        public void modifyPsw(
                String verifyCode,
                String password,
                String rePassword,
                String scene);
    }
}
