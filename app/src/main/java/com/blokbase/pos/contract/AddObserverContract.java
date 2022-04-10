package com.blokbase.pos.contract;

import com.common.lib.bean.ObserverBean;
import com.common.lib.mvp.IPresenter;
import com.common.lib.mvp.IView;

import java.util.HashMap;

public interface AddObserverContract {

    public interface View extends IView {
        public void addObserverSuccess(ObserverBean observer);
    }

    public interface Presenter extends IPresenter {
        public void addObserver(String type, String remark, HashMap<String,Boolean> permission);
    }
}
