package com.blokbase.pos.contract;

import com.common.lib.bean.QuestionBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface ProtocolContract {

    public interface View extends IView {
        public void getProtocolSuccess(QuestionBean bean);
    }

    public interface Presenter extends IPresenter {
        public void protocol();
    }
}
