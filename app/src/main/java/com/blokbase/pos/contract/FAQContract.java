package com.blokbase.pos.contract;

import com.common.lib.bean.QuestionBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;

public interface FAQContract {

    public interface View extends IView {
        public void getFaqSuccess(ArrayList<QuestionBean> list);
    }

    public interface Presenter extends IPresenter {
        public void faq(int pageIndex);
    }
}
