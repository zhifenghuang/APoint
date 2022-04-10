package com.blokbase.pos.contract;

import com.common.lib.bean.QuestionBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface ServiceHelpContract {

    public interface View extends IView {
        public void getServiceHelpSuccess(QuestionBean poster);
    }

    public interface Presenter extends IPresenter {
        public void serviceHelp();
    }
}
