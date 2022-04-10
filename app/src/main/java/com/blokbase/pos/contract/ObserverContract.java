package com.blokbase.pos.contract;

import com.common.lib.bean.ObserverBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

public interface ObserverContract {

    public interface View extends IView {
        public void addObserverSuccess(int pageIndex, ArrayList<ObserverBean> list);
        public void addObserverFailed();
        public void deleteObserverSuccess(ObserverBean bean);
    }

    public interface Presenter extends IPresenter {
        public void getObservers(int pageIndex, String type);
        public void deleteObserver(ObserverBean bean);
    }
}
