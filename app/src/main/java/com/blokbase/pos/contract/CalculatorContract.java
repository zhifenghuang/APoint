package com.blokbase.pos.contract;

import com.common.lib.bean.CalculatorBean;
import com.common.lib.bean.MetaBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

public interface CalculatorContract {

    public interface View extends IView {
        public void getMetaSuccess(MetaBean bean);
    }

    public interface Presenter extends IPresenter {
        public void appMeta();
    }
}
